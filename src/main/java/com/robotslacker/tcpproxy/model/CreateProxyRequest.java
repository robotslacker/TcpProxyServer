package com.robotslacker.tcpproxy.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString()
public class CreateProxyRequest {
	String                      proxyName;
	int                         localPort;
	List<ProxyTargetEndPoint>   proxyTargetEndPoint;
	int                         workerCount;
}
