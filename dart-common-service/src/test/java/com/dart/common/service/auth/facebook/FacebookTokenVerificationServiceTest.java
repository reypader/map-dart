package com.dart.common.service.auth.facebook;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.http.WebClient;
import com.dart.common.service.properties.PropertiesProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class FacebookTokenVerificationServiceTest {

    private WebClient mockWebClient = mock(WebClient.class);
    private PropertiesProvider mockProperties = mock(PropertiesProvider.class);
    private ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp() throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 1);
        long later = now.getTimeInMillis() / 1000;

        when(mockWebClient.get(anyString(), eq(Collections.<String, Object>emptyMap()))).thenReturn(new ByteArrayInputStream("token=TOKEN".getBytes()),
                                                                                                    new ByteArrayInputStream(
                                                                                                            ("{data:{application:'Pings',expires_at:" + later + ",user_id:123,is_valid:true}}").getBytes()));
        when(mockProperties.getFacebookAppId()).thenReturn("FB_APP_ID");
        when(mockProperties.getFacebookSecret()).thenReturn("FB_SECRET");
        when(mockProperties.getFacebookEndpoint()).thenReturn("http://www.derp");
        when(mockProperties.getAppName()).thenReturn("Pings");
    }

    @Test
    public void testVerifyToken() throws Exception {
        TokenVerificationService service = new FacebookTokenVerificationService(mockWebClient, mockProperties);
        assertTrue(service.verifyToken("test", "123"));

        verify(mockWebClient, times(2)).get(pathCaptor.capture(), any(Map.class));
        List<String> caughtValues = pathCaptor.getAllValues();
        String getAccessToken = caughtValues.get(0);
        assertEquals(
                "http://www.derp/oauth/access_token?client_id=FB_APP_ID&client_secret=FB_SECRET&grant_type=client_credentials",
                getAccessToken);
        String getTokenDetails = caughtValues.get(1);
        assertEquals("http://www.derp/debug_token?input_token=test&access_token=TOKEN",
                     getTokenDetails);
    }


}