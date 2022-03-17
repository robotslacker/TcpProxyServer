package com.robotslacker.tcpproxy.tcpserver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class TcpServerWorker extends Thread {

    private final static long SELECTOR_TIMEOUT = 100L;
    private final static Logger LOGGER = Logger.getAnonymousLogger();

    private final Queue<ITcpServerHandler> handlers;

    public TcpServerWorker(final Queue<ITcpServerHandler> handlers) {
        super("TcpServerWorker");
        this.handlers = handlers;
    }

    @Override
    public void run() {
        Selector selector = null;
        try {
            selector = Selector.open();

            while (!Thread.interrupted()) {
                ITcpServerHandler newHandler = handlers.poll();
                if (newHandler != null) {
                    newHandler.register(selector);
                }

                selector.select(SELECTOR_TIMEOUT);

                final Set<SelectionKey> keys = selector.selectedKeys();
                for (final SelectionKey key : keys) {
                    final ITcpServerHandler handler = (ITcpServerHandler) key.attachment();
                    handler.process(key);
                }
                keys.clear();
            }
        } catch (final IOException exception) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Problem with selector, worker will be stopped!", exception);
        } finally {
            if (selector != null) {
                closeSelector(selector);
            }
        }
    }

    private void closeSelector(Selector selector) {
        for (final SelectionKey key : selector.keys())
        {
            try {
                key.channel().close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
