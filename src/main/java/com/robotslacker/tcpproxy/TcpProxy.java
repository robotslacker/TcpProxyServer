package com.robotslacker.tcpproxy;

import com.robotslacker.tcpproxy.model.TcpProxyConfig;

/**
 * TCP proxy.
 *
 * After starting, it's listening local port and send all incoming
 * traffic on it from client to remote host and from remote host to client.
 * Doesn't have any timeout. If client or remote server closes connection it will
 * close opposite connection.
 *
 * Multi-thread and asynchronous TCP proxy server based on NIO.
 *
 * You can create any count of proxy instances and run they in together.
 *
 * @see TcpProxyConnectorFactory
 * @see TcpProxyConnector
 * @see StaticTcpProxyConfig
 * @see TcpServer
 */
public class TcpProxy {

    private final TcpServer server;

    public TcpProxy(final TcpProxyConfig config) {
        TcpProxyConnectorFactory handlerFactory = new TcpProxyConnectorFactory(config);

        final TcpServerConfig serverConfig =
                new TcpServerConfig(config.getLocalPort(), handlerFactory, config.getWorkerCount());

        server = new TcpServer(serverConfig);
    }

    /**
     * Start server.
     * This method run servers worked then return control.
     * This method isn't blocking.
     *
     * If you call this method when server is started, it throws exception.
     *
     * See {@link TcpServer#start()}
     */
    public void start() {
        server.start();
    }

    /**
     * Stop server and release all resources.
     * If server already been closed this method return immediately
     * without side effects.
     *
     * See {@link TcpServer#shutdown()}
     */
    public void shutdown() {
        server.shutdown();
    }

}
