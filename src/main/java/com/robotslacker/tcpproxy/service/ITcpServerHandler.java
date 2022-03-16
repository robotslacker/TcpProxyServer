package com.robotslacker.tcpproxy.service;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Handler for all incoming client connection.
 *
 * @see TcpServerHandlerFactory
 */
public interface ITcpServerHandler {

    /**
     * Called when worker get handler from queue.
     * 
     * You can use this method for register it channel on
     * selector.
     * 
     * This method called only one time.
     *
     * @param selector - selector which will support this handler.
     */
    void register(Selector selector);

    /**
     * Called when selector receive IO event
     * it tries to get attached handler from key and call
     * this method.
     *
     * @param key - event from IO
     */
    void process(SelectionKey key);

    /**
     * Called when workers were stopped and server should
     * close all not processed channels.
     * 
     * Called only one time.
     */
    void destroy();

}
