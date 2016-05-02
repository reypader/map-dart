package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.http.WebClient;
import com.dart.common.service.property.PropertiesProvider;
import com.google.common.net.MediaType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class RecaptchaTokenVerificationServiceTest {

    private WebClient mockWebClient = mock(WebClient.class);
    private PropertiesProvider mockProperties = mock(PropertiesProvider.class);

    @Before
    public void setUp() throws Exception {
        when(mockWebClient.post(anyString(), any(MediaType.class), any(byte[].class),
                                eq(Collections.<String, Object>emptyMap()))).thenReturn(
                new ByteArrayInputStream(("{\"success\":true,\"error-codes\":[]}").getBytes()));

        PropertiesProvider.ThirdPartyApi mockRecaptcha = mock(PropertiesProvider.ThirdPartyApi.class);
        when(mockProperties.getRecaptcha()).thenReturn(mockRecaptcha);
        when(mockRecaptcha.getSecret()).thenReturn("recaptcha_secret");
        when(mockRecaptcha.getEndpoint()).thenReturn("http://www.derp");
    }

    @Test
    public void testVerifyToken() throws Exception {
        TokenVerificationService service = new RecaptchaTokenVerificationService(mockWebClient, mockProperties);
        assertTrue(service.verifyToken("test", "123"));

        String data = "secret=recaptcha_secret&response=test&remoteip=123";

        verify(mockWebClient, times(1)).post(eq("http://www.derp"), eq(MediaType.FORM_DATA), eq(data.getBytes()),
                                             anyMap());
    }
}