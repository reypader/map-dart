package com.dart.common.service.auth;

import com.dart.common.service.auth.google.GoogleTokenVerificationService;
import com.dart.common.service.properties.PropertiesProvider;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author RMPader
 */
public class GoogleTokenVerificationServiceTest {

    @Ignore //No idea how to effectively test this. I will trust in Google
    @Test
    public void testVerifyToken() throws Exception {
        PropertiesProvider mockProps = mock(PropertiesProvider.class);
        when(mockProps.getGplusAppId()).thenReturn("");

        TokenVerificationService service = new GoogleTokenVerificationService(mockProps);
        assertTrue(service.verifyToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjQyMzNiZWY4YTAyMGE5OTA4MjM4MzVhYTQzYjliMzk3ZmU1YWEyNDcifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXRfaGFzaCI6IjZYUnE2OUw4OWs3VEpBaERlbGsyV2ciLCJhdWQiOiIyMTgxNzgzMDY2ODYtODNwcW05ZzJhN3MyMTRocDAza3Y1MWxjZ201bm5paWQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDEwNjI3MDE5NzgwNDgzNTg0OTQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXpwIjoiMjE4MTc4MzA2Njg2LTgzcHFtOWcyYTdzMjE0aHAwM2t2NTFsY2dtNW5uaWlkLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJyZXluYWxkbXBhZGVyQGdtYWlsLmNvbSIsImlhdCI6MTQzOTM2OTI5NCwiZXhwIjoxNDM5MzcyODk0LCJuYW1lIjoiUmV5bmFsZCBQYWRlciIsImdpdmVuX25hbWUiOiJSZXluYWxkIiwiZmFtaWx5X25hbWUiOiJQYWRlciIsImxvY2FsZSI6ImVuIn0.c7SuvkyrkbGk81zzzVAMtSASoj88fXcmEXSAI0E1sYVjnrkuj6ntagGairTyVAVblyW9NJG2ItRcWL0EvyKui-OzB2Fjbyp45IKAoMktjdXnv76oxPgPJhJzqMKjb4Kfk0lo7oBW7XrINbq_QwVfm7xpeB5tCtngyf2FLKg5PIrVgkEAxZG79NOdHl6ZFDg2yHYJck44sN5sN3Shk8-lugoyPj5WTdpsf8utYYX-_G2X9r4KXD3rEIBn2OuzU3Sbon1sZ8exdM52rfGiP5pX3pp-x3KBSP2QZYUJlFf6ijCzz6XvEDUqZJ4__bQUT1M0H_LVWVTkyUsakR_lphulKA", "reynaldmpader@gmail.com"));
    }


}