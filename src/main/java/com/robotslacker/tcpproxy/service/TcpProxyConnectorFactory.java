package com.robotslacker.tcpproxy.service;

import java.nio.channels.SocketChannel;

class TcpProxyConnectorFactory implements TcpServerHandlerFactory {

    private final ITcpProxyService config;

    public TcpProxyConnectorFactory(ITcpProxyService config) {
        this.config = config;
    }

    @Override
    public ITcpServerHandler create(final SocketChannel clientChannel) {
        return new TcpProxyConnector(clientChannel, config);
    }

}
