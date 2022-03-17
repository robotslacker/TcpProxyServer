package com.robotslacker.tcpproxy.service;

import com.robotslacker.tcpproxy.tcpserver.ITcpServerHandler;
import com.robotslacker.tcpproxy.tcpserver.ITcpServerHandlerFactory;

import java.nio.channels.SocketChannel;

class TcpProxyConnectorFactory implements ITcpServerHandlerFactory {

    private final ITcpProxyService config;

    public TcpProxyConnectorFactory(ITcpProxyService config) {
        this.config = config;
    }

    @Override
    public ITcpServerHandler create(final SocketChannel clientChannel) {
        return new TcpProxyConnector(clientChannel, config);
    }

}
