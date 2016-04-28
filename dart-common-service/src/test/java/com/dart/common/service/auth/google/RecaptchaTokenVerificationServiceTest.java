package com.dart.common.service.auth.google;

import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author RMPader
 */
public class RecaptchaTokenVerificationServiceTest {

    private HttpClient mockHttpClient = mock(HttpClient.class);
    private PropertiesProvider mockProperties = mock(PropertiesProvider.class);
    private ArgumentCaptor<HttpPost> getCaptor = ArgumentCaptor.forClass(HttpPost.class);

    @Before
    public void setUp() throws Exception {
        when(mockHttpClient.execute(any(HttpPost.class), any(ResponseHandler.class))).thenReturn("{\"success\":true,\"error-codes\":[]}");
        when(mockProperties.getRecaptchaSecret()).thenReturn("recaptcha_secret");
        when(mockProperties.getRecaptchaEndpoint()).thenReturn("http://www.derp");
    }

    @Test
    public void testVerifyToken() throws Exception {
        TokenVerificationService service = new RecaptchaTokenVerificationService(mockHttpClient, mockProperties);
        assertTrue(service.verifyToken("test", "123"));

        verify(mockHttpClient, times(1)).execute(getCaptor.capture(), any(ResponseHandler.class));
        List<HttpPost> caughtValues = getCaptor.getAllValues();
        HttpPost recaptcha = caughtValues.get(0);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("secret", "recaptcha_secret"));
        nvps.add(new BasicNameValuePair("response", "test"));
        nvps.add(new BasicNameValuePair("remoteip", "123"));

        String theString = convertStreamToString(new UrlEncodedFormEntity(nvps).getContent());
        assertEquals(theString, convertStreamToString(recaptcha.getEntity().getContent()));
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}