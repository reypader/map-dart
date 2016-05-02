package com.dart.photo.api.endpoint;

import com.dart.data.domain.User;
import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author RMPader
 */
@RestController
@RequestMapping("/photo")
public class PhotoEndpoint {

    @Autowired
    private UploadService service;

    @RequestMapping(value = "/upload/create",
                    method = RequestMethod.GET)
    public CreateUploadURLResponse createUploadURL(@AuthenticationPrincipal User user, @RequestParam("md5") String md5,
                                                   @RequestParam("type") String type) {
        return service.getUploadURL(md5, type, user.getId());
    }

}
