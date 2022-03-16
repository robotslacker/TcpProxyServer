package com.robotslacker.tcpproxy.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;
import org.slf4j.Marker;

public class MarkerNameBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
    private static final String KEY = "markerName";

    public String getKey() {
        return KEY;
    }

    public String getDiscriminatingValue(ILoggingEvent e) {
        Marker eventMarker = e.getMarker();
        if (eventMarker == null) {
            return "CloudService";
        }
        return eventMarker.getName();
    }
}