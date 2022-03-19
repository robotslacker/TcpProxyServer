package com.robotslacker.tcpproxy.service.impl;

import com.robotslacker.tcpproxy.model.ProxyTargetEndPoint;
import com.robotslacker.tcpproxy.service.ITcpProxyService;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * TCP proxy configuration.
 */
@Data
@ToString()
public class TcpProxyServiceStaticIImpl implements ITcpProxyService {

    private final int localPort;
    private final String remoteHost;
    private final int remotePort;
    private int workerCount;
    private String proxyName;
    List<ProxyTargetEndPoint> proxyTargetEndPointList = new ArrayList<>();

    public TcpProxyServiceStaticIImpl(int localPort, String remoteHost, int remotePort, int workerCount) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.workerCount = workerCount;
        ProxyTargetEndPoint proxyTargetEndPoint = new ProxyTargetEndPoint();
        proxyTargetEndPoint.setTargetPort(remotePort);
        proxyTargetEndPoint.setTargetAddress(remoteHost);
        this.proxyTargetEndPointList.add(proxyTargetEndPoint);
    }
}
