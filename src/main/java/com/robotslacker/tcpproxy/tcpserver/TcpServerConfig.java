package com.robotslacker.tcpproxy.tcpserver;

/**
 * Configuration for TCP server
 */
public class TcpServerConfig {

    private final int port;
    private final int workerCount;
    private final ITcpServerHandlerFactory handlerFactory;

    public TcpServerConfig(int port, ITcpServerHandlerFactory handlerFactory, int workerCount) {
        if (workerCount < 1)
            throw new IllegalArgumentException("Count of workers should be at least 1!");

        if (port < 0)
            throw new IllegalArgumentException("Port can't be negative!");

        if (handlerFactory == null)
            throw new NullPointerException("Please specify handler factory!");

        this.port = port;
        this.workerCount = workerCount;
        this.handlerFactory = handlerFactory;
    }

    /**
     * @return - local port which TCP server will be listening, should be 0..64000
     */
    public int getPort() {
        return port;
    }

    /**
     * @return - handler factory which TCP server will use for process incoming connections
     */
    public ITcpServerHandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    /**
     * @return - count of worker (thread) which TCP proxy will use for processing
     *         incoming client connection, should more 0
     */
    public int getWorkerCount() {
        return workerCount;
    }

}
