package com.robotslacker.tcpproxy.service;

import com.robotslacker.tcpproxy.model.ProxyTargetEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

class TcpProxyConnector implements ITcpServerHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TcpProxyBuffer clientBuffer = new TcpProxyBuffer();
    private final TcpProxyBuffer serverBuffer = new TcpProxyBuffer();
    private final SocketChannel clientChannel;

    private Selector selector;
    private SocketChannel serverChannel;
    private final ITcpProxyService config;
    private String clientAddress = "";
    private String serverAddress = "";

    public TcpProxyConnector(SocketChannel clientChannel, ITcpProxyService config) {
        this.clientChannel = clientChannel;
        try {
            this.clientAddress = clientChannel.getRemoteAddress().toString();
        }
        catch (IOException ignored){}
        this.config = config;
    }

    public void readFromClient() throws IOException {
        serverBuffer.writeFrom(clientChannel);
        if (serverBuffer.isReadyToRead()) register();
    }

    public void readFromServer() throws IOException {
        clientBuffer.writeFrom(serverChannel);
        if (clientBuffer.isReadyToRead()) register();
    }

    public void writeToClient() throws IOException {
        clientBuffer.writeTo(clientChannel);
        if (clientBuffer.isReadyToWrite()) register();
    }

    public void writeToServer() throws IOException {
        serverBuffer.writeTo(serverChannel);
        if (serverBuffer.isReadyToWrite()) register();
    }

    public void register() throws ClosedChannelException {
        int clientOps = 0;
        if (serverBuffer.isReadyToWrite()) clientOps |= SelectionKey.OP_READ;
        if (clientBuffer.isReadyToRead()) clientOps |= SelectionKey.OP_WRITE;
        clientChannel.register(selector, clientOps, this);

        int serverOps = 0;
        if (clientBuffer.isReadyToWrite()) serverOps |= SelectionKey.OP_READ;
        if (serverBuffer.isReadyToRead()) serverOps |= SelectionKey.OP_WRITE;
        serverChannel.register(selector, serverOps, this);
    }

    private void closeQuietly(SocketChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException exception) {
                logger.error("无法正常关闭隧道【】",
                    exception);
            }
        }
    }

    @Override
    public void register(Selector selector) {
        this.selector = selector;

        ProxyTargetEndPoint proxyTargetEndPoint = config.getProxyTargetEndPointList().get(0);
        this.serverAddress = proxyTargetEndPoint.getTargetAddress() + ":" + proxyTargetEndPoint.getTargetPort();

        try {
            clientChannel.configureBlocking(false);

            final InetSocketAddress socketAddress = new InetSocketAddress(
                    proxyTargetEndPoint.getTargetAddress(), proxyTargetEndPoint.getTargetPort());
            serverChannel = SocketChannel.open();
            serverChannel.connect(socketAddress);
            serverChannel.configureBlocking(false);

            register();
            logger.info("成功建立从客户端点【{}】到目标端点【{}】的隧道.",
                clientChannel.getRemoteAddress(),
                "/" + proxyTargetEndPoint.getTargetAddress() + ":" + proxyTargetEndPoint.getTargetPort());
        } catch (ConnectException ce)
        {
            destroy();
            logger.info("无法连接到指定的目标端点【{}:{}】",
                proxyTargetEndPoint.getTargetAddress(), proxyTargetEndPoint.getTargetPort());
        }
        catch (final IOException exception) {
            destroy();
            logger.error("无法连接到指定的目标端点【{}:{}】",
                proxyTargetEndPoint.getTargetAddress(),
                proxyTargetEndPoint.getTargetPort(),
                exception);
        }
    }

    @Override
    public void process(final SelectionKey key) {
        try {
            if (key.channel() == clientChannel) {
                if (key.isValid() && key.isReadable()) {
                    readFromClient();
                }
                if (key.isValid() && key.isWritable()) {
                    writeToClient();
                }
            }
        } catch (final ClosedChannelException exception) {
            destroy();
            logger.error("Channel was closed by client or server.", exception);
        } catch (final IOException exception) {
            destroy();
            logger.info("来自于客户端【{}】的连接已经中断.", clientAddress);
        }


        try {
            if (key.channel() == serverChannel) {
                if (key.isValid() && key.isReadable()) {
                    readFromServer();
                }
                if (key.isValid() && key.isWritable()) {
                    writeToServer();
                }
            }
        } catch (final ClosedChannelException exception) {
            destroy();
            logger.error("Channel was closed by client or server.", exception);
        } catch (final IOException exception) {
            destroy();
            logger.info("来自于服务端【{}】的连接已经中断.", serverAddress);
        }

    }

    @Override
    public void destroy() {
        closeQuietly(clientChannel);
        closeQuietly(serverChannel);
    }
}
