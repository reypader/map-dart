package com.dart.common.service.auth;

import com.dart.common.service.auth.facebook.FacebookTokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class FacebookTokenVerificationServiceTest {

    private HttpClient mockHttpClient = mock(HttpClient.class);
    private PropertiesProvider mockProperties = mock(PropertiesProvider.class);
    private ArgumentCaptor<HttpGet> getCaptor = ArgumentCaptor.forClass(HttpGet.class);

    @Before
    public void setUp() throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 1);
        long later = now.getTimeInMillis()/1000;
        when(mockHttpClient.execute(any(HttpGet.class), any(ResponseHandler.class))).thenReturn("token=TOKEN", "{data:{application:'Pings',expires_at:" + later + ",user_id:123}}");
        when(mockProperties.getFacebookAppId()).thenReturn("FB_APP_ID");
        when(mockProperties.getFacebookSecret()).thenReturn("FB_SECRET");
        when(mockProperties.getFacebookEndpoint()).thenReturn("http://www.derp");
        when(mockProperties.getAppName()).thenReturn("Pings");
    }

    @Test
    public void testVerifyToken() throws Exception {
        TokenVerificationService service = new FacebookTokenVerificationService(mockHttpClient, mockProperties);
        assertTrue(service.verifyToken("test", "123"));

        verify(mockHttpClient, times(2)).execute(getCaptor.capture(), any(ResponseHandler.class));
        List<HttpGet> caughtValues = getCaptor.getAllValues();
        HttpGet getAccessToken = caughtValues.get(0);
        assertEquals(new URI("http://www.derp/oauth/access_token?client_id=FB_APP_ID&client_secret=FB_SECRET&grant_type=client_credentials"), getAccessToken.getURI());
        HttpGet getTokenDetails = caughtValues.get(1);
        assertEquals(new URI("http://www.derp/debug_token?input_token=test&access_token=TOKEN"), getTokenDetails.getURI());
    }


}