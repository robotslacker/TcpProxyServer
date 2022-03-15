package com.robotslacker.tcpproxy.model;


public interface TcpProxyConfig {
  int getLocalPort();

  int getRemotePort();

  String getRemoteHost();

  int getWorkerCount();

  void setWorkerCount(int workerCount);
}
