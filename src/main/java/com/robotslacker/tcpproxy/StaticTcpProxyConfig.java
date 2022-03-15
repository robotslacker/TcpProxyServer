package com.robotslacker.tcpproxy;

import com.robotslacker.tcpproxy.model.TcpProxyConfig;

/**
 * TCP proxy configuration.
 */
public class StaticTcpProxyConfig implements TcpProxyConfig {

    private final int localPort;
    private final String remoteHost;
    private final int remotePort;
    private int workerCount;

    public StaticTcpProxyConfig(int localPort, String remoteHost, int remotePort) {
        this(localPort, remoteHost, remotePort, 0);
    }

    public StaticTcpProxyConfig(int localPort, String remoteHost, int remotePort, int workerCount) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.workerCount = workerCount;
    }

    /**
     * @return - local port which TCP proxy will be listening, should be 0..64000
     */
    @Override public int getLocalPort() {
        return localPort;
    }

    /**
     * @return - remote port on which TCP proxy will send all traffic from incoming connections
     */
    @Override public int getRemotePort() {
        return remotePort;
    }

    /**
     * @return - remote host on which TCP proxy will send all traffic from incoming connections
     */
    @Override public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * @return - count of worker (thread) which TCP proxy will use for processing
     *         incoming client connection, should more 0
     */
    @Override public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

}
