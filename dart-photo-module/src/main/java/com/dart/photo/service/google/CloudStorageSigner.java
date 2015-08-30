package com.dart.photo.service.google;

import org.apache.commons.codec.binary.Base64;

import javax.inject.Singleton;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

/**
 * @author RMPader
 */
@Singleton
public class CloudStorageSigner {

    private final PrivateKey key;

    public CloudStorageSigner() {
        this.key = loadKeyFromPkcs12();
    }

    private PrivateKey loadKeyFromPkcs12() {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(this.getClass().getClassLoader().getResourceAsStream("key.p12"), "notasecret".toCharArray());
            return (PrivateKey) ks.getKey("privatekey", "notasecret".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String signString(String method, String md5, String contentType, String expiration, String canonicalHeaders, String path) {
        try {
            String stringToSign = method + "\n" + md5 + "\n" + contentType + "\n" + expiration + "\n" + canonicalHeaders;
            if (stringToSign.endsWith("\n")) {
                stringToSign = stringToSign + path;
            } else {
                stringToSign = stringToSign + "\n" + path;
            }
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(key);
            signer.update(stringToSign.getBytes("UTF-8"));
            byte[] rawSignature = signer.sign();
            return new String(Base64.encodeBase64(rawSignature, false), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
