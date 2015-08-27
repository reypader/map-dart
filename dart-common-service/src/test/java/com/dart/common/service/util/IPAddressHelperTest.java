package com.dart.common.service.util;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author RMPader
 */
public class IPAddressHelperTest {


    @Test
    public void testGetIPAddress(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        assertEquals("127.0.0.1", IPAddressHelper.getIPAddress(request));
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1,192.168.0.1");
        assertEquals("127.0.0.1", IPAddressHelper.getIPAddress(request));
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1, 192.168.0.1");
        assertEquals("127.0.0.1", IPAddressHelper.getIPAddress(request));
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1");
        assertEquals("127.0.0.1", IPAddressHelper.getIPAddress(request));
    }


}