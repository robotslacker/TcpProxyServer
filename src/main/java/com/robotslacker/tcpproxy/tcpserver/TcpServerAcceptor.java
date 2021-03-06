package com.robotslacker.tcpproxy.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

class TcpServerAcceptor implements ITcpServerHandler {

    private final static int ACCEPT_BUFFER_SIZE = 1000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TcpServerConfig config;
    private final Queue<ITcpServerHandler> handlers;

    public TcpServerAcceptor(final TcpServerConfig config,
                             final Queue<ITcpServerHandler> handlers) {
        this.config = config;
        this.handlers = handlers;
    }

    @Override
    public void register(final Selector selector) {
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(config.getPort()), ACCEPT_BUFFER_SIZE);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT, this);
        } catch (final IOException exception) {
            logger.info("无法打开端口【{}】的服务监听.【{}】",
                config.getPort(),
                exception.getMessage());
        }
    }

    @Override
    public void process(SelectionKey key) {
        if (key.isValid() && key.isAcceptable()) {
            try {
                final ServerSocketChannel server = (ServerSocketChannel) key.channel();

                SocketChannel clientChannel;
                clientChannel = server.accept();

                handlers.add(config.getHandlerFactory().create(clientChannel));
            } catch (final IOException exception) {
                logger.info("无法注册端口【{}】的服务业务.【{}】",
                    config.getPort(),
                    exception.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        // nothing
    }

}
