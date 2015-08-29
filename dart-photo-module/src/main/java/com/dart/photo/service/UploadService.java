package com.dart.photo.service;

import com.dart.photo.api.CreateUploadURLResponse;

/**
 * @author RMPader
 */
public interface UploadService {
    CreateUploadURLResponse getUploadURL(String md5, String type, String identifier);
}
