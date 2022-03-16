package com.robotslacker.tcpproxy.service;

import com.robotslacker.tcpproxy.config.TcpServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Simple TCP server based on NIO.
 *
 * Server use workers for process incoming client connections.
 *
 * Worker is thread, it waits on own selector {@link java.nio.channels.Selector}
 *
 * Only one worker processes accept for incoming client connection, after
 * that this worker uses @{link TcpServerHandlerFactory} for create
 * handler @{link TcpServerHandler} and add it to not started handlers queue. All workers
 * have access to this queue.
 *
 * Worker has next lifecycle: try to get one not started handler from queue
 * if it exists register it, then wait on selector with timeout, get IO events
 * for each event get attached handler from key and process it.
 * After that worker returns to step with queue.
 *
 * @see TcpServerConfig
 * @see ITcpServerHandler
 * @see TcpServerHandlerFactory
 */
public class TcpServer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TcpServerConfig config;

    private Queue<ITcpServerHandler> handlers;
    private Thread[] workers;

    /**
     * @param config - config
     * @throws IllegalArgumentException - when worker count less than 1
     */
    public TcpServer(final TcpServerConfig config) {
        if (config == null)
            throw new NullPointerException("Please specify config! Thx!");

        this.config = config;
    }

    /**
     * This method starts waiting incoming connections for proxy to remote host.
     * Method return control when all worker will be started, it isn't block.
     *
     * @throws UnsupportedOperationException - if you try to start already started connector
     */
    public void start() {
        if (workers != null) throw new UnsupportedOperationException("Please shutdown connector!");

        handlers = new ConcurrentLinkedQueue<>();
        handlers.add(new TcpServerAcceptor(config, handlers));

        workers = new Thread[config.getWorkerCount()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new TcpServerWorker(handlers);
        }

        for (final Thread worker : workers) worker.start();

        logger.info("TCP服务器已经启动在端口【{}】，共包含工作线程【{}】个.", config.getPort(), config.getWorkerCount());
    }

    /**
     * Shutdown connector.
     *
     * This method wait when all resources will be closed.
     * You can call this method any time.
     * No problem and exceptions if you try to shut down connector twice without start.
     */
    public void shutdown() {
        if (workers == null) {
            logger.info("TCP服务器已经启动在端口【{}】已经被关闭.", config.getPort());
            return;
        }

        logger.info("准备关闭在端口【{}】上的TCP服务器...", config.getPort());
        for (final Thread worker : workers) {
            worker.interrupt();
            try {
                worker.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
        ITcpServerHandler handler;
        while ((handler = handlers.poll()) != null) handler.destroy();
        handlers = null;
        logger.info("在端口【{}】上的TCP服务器已经被关闭.", config.getPort());
        workers = null;
    }
}
