package com.dart.photo.service.google;

import com.dart.common.service.properties.PropertiesProvider;
import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.api.UploadResponse;
import com.dart.photo.service.UploadService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.inject.Inject;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author RMPader
 */
@Singleton
public class CloudStorageUploadService implements UploadService {

    private final PropertiesProvider propertiesProvider;
    private final BlobstoreService blobstoreService;

    @Inject
    public CloudStorageUploadService(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
        this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    }

    @Override
    public CreateUploadURLResponse getGoogleCloudStorageUploadURL(String handlerURL) {
        UploadOptions options = UploadOptions.Builder
                .withGoogleStorageBucketName(propertiesProvider.getGoogleCloudStorageBucket())
                .maxUploadSizeBytes(propertiesProvider.getMaxFileUploadByteSize());
        String s = blobstoreService.createUploadUrl(handlerURL, options);
        CreateUploadURLResponse response = new CreateUploadURLResponse();
        response.setUploadURL(s);
        return response;
    }

    @Override
    public UploadResponse handleGoogleCloudStorageUpload(HttpServletRequest request) {
        List<BlobKey> blobs = blobstoreService.getUploads(request).get("file");
        BlobKey blobKey = blobs.get(0);

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

        UploadResponse response = new UploadResponse();
        response.setServingURL(imagesService.getServingUrl(servingOptions));

        return response;
    }
}
