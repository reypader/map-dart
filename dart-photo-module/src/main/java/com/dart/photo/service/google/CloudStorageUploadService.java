package com.dart.photo.service.google;

import com.dart.common.service.properties.PropertiesProvider;
import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.service.UploadService;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author RMPader
 */
@Singleton
public class CloudStorageUploadService implements UploadService {

    private final PropertiesProvider propertiesProvider;
    private final CloudStorageSigner signer;

    @Inject
    public CloudStorageUploadService(PropertiesProvider propertiesProvider, CloudStorageSigner signer) {
        this.propertiesProvider = propertiesProvider;
        this.signer = signer;
    }

    @Override
    public CreateUploadURLResponse getUploadURL(String md5, String type, String identifier) {
        try {
            Calendar later = Calendar.getInstance();
            later.add(Calendar.SECOND, 300);
            String expiration = String.valueOf(later.getTimeInMillis() * 1000);
            String filename = identifier + "_" + UUID.randomUUID().toString();
            String path = "/" + propertiesProvider.getGoogleCloudStorageBucket() + "/" + filename;
            String url_signature = signer.signString("PUT", md5, type, expiration, "x-goog-usr:" + identifier, path);

            String signed_url = propertiesProvider.getGoogleCloudStorageURL() + path +
                    "?GoogleAccessId=" + propertiesProvider.getGoogleServiceAccount() +
                    "&Expires=" + expiration +
                    "&Signature=" + URLEncoder.encode(url_signature, "UTF-8");
            CreateUploadURLResponse response = new CreateUploadURLResponse();
            response.setUploadURL(signed_url);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
