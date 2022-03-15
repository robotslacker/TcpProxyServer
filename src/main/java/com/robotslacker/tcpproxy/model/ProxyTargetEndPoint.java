package com.robotslacker.tcpproxy.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString()
public class ProxyTargetEndPoint {
	int     targetPort;
	String  targetAddress;
}
