package com.dart.photo.service;

import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.api.UploadResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public interface UploadService {
    CreateUploadURLResponse getGoogleCloudStorageUploadURL(String handlerURL);
    UploadResponse handleGoogleCloudStorageUpload(HttpServletRequest request);
}
