package com.robotslacker.tcpproxy.tcpserver;

import java.nio.channels.SocketChannel;

/**
 * TCP server uses this class for create handler for
 * all incoming connection from clients. When handler
 * was created TCP server use it for process events from client channel.
 */
public interface ITcpServerHandlerFactory {

    ITcpServerHandler create(SocketChannel clientChannel);

}
