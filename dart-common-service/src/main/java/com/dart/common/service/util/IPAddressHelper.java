package com.dart.common.service.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public class IPAddressHelper {

    public static String getIPAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
}
