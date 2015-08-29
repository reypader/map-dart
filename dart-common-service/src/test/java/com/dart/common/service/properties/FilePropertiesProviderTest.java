package com.dart.common.service.properties;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author RMPader
 */
public class FilePropertiesProviderTest {
    PropertiesProvider props = new FilePropertiesProvider(getFileStream("test.testprops"));

    private InputStream getFileStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    @Test
    public void testGetGoogleAppId() throws Exception {
        assertEquals("gaeid", props.getGoogleAppId());
    }


    @Test
    public void testGetGplusAppId() throws Exception {
        assertEquals("gplusid", props.getGplusAppId());
    }

    @Test
    public void testGetGplusAppSecret() throws Exception {
        assertEquals("gplusecret", props.getGplusAppSecret());
    }

    @Test
    public void testGetGplusEndpoint() throws Exception {
        assertEquals("gplusendpoint", props.getGplusEndpoint());
    }

    @Test
    public void testGetFacebookAppId() throws Exception {
        assertEquals("fbid", props.getFacebookAppId());
    }

    @Test
    public void testGetFacebookSecret() throws Exception {
        assertEquals("fbsecret", props.getFacebookSecret());
    }

    @Test
    public void testGetFacebookEndpoint() throws Exception {
        assertEquals("fbendpoint", props.getFacebookEndpoint());
    }

    @Test
    public void testGetDefaultTokenValidityDays() throws Exception {
        assertEquals(1, props.getDefaultTokenValidityDays());
    }

    @Test
    public void testGetSignupEmailTemplate() throws Exception {
        assertEquals("email", props.getSignupEmailTemplate());
    }

    @Test
    public void testGetUserWebsiteURL() throws Exception {
        assertEquals("web", props.getUserWebsiteURL());
    }

    @Test
    public void testGetRecaptchaEndpoint() throws Exception {
        assertEquals("recaptchaendpoint", props.getRecaptchaEndpoint());
    }

    @Test
    public void testGetAppName() throws Exception {
        assertEquals("Pings", props.getAppName());
    }

    @Test
    public void testGetRecaptchaSecret() throws Exception {
        assertEquals("recaptchasecret", props.getRecaptchaSecret());
    }

    @Test
    public void testGetGoogleServiceAccount() throws Exception {
        assertEquals("serviceacct", props.getGoogleServiceAccount());
    }

    @Test
    public void testGetGoogleCloudStorageURL() throws Exception {
        assertEquals("gcsurl",props.getGoogleCloudStorageURL());
    }

    @Test
    public void testGetGoogleCloudStorageBucket() throws Exception {
        assertEquals("gcsbuck",props.getGoogleCloudStorageBucket());
    }
}