package com.robotslacker.tcpproxy.service;


import com.robotslacker.tcpproxy.model.ProxyTargetEndPoint;

import java.util.List;

public interface ITcpProxyService
{
    String  getProxyName();

    int     getWorkerCount();
    int     getLocalPort();

    List<ProxyTargetEndPoint> getProxyTargetEndPointList();

}
