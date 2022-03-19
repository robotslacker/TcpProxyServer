package com.robotslacker.tcpproxy.tcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;

class TcpServerWorker extends Thread {

    private final static long SELECTOR_TIMEOUT = 100L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.error("TCP服务Worker出现异常错误，Worker已经被停止!", exception);
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
