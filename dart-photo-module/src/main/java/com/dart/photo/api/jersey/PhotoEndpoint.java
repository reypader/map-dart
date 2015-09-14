package com.dart.photo.api.jersey;

import com.dart.common.service.aop.Authenticated;
import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author RMPader
 */
@Path("/photo")
@Api(value = "/photo", description = "API for doing user-related actions such as CRUD, and authentication")
public class PhotoEndpoint {
    private UploadService service;

    @Inject
    public PhotoEndpoint(UploadService service) {
        this.service = service;
    }

    @Authenticated
    @GET
    @Path("/upload/gcs/create")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Generate URL that can be used for uploading a file to Google Cloud Storage.",
            response = CreateUploadURLResponse.class)
    public CreateUploadURLResponse createGCSUploadURL(@Context HttpServletRequest httpRequest) {
        return service.getGoogleCloudStorageUploadURL("/upload");
    }

//    @Authenticated
//    @POST
//    @Path("/upload/gcs/handle")
//    @Produces(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "Handle the upload response from GCS and create the serving URL for the file.",
//            response = CreateUploadURLResponse.class)
//    public UploadResponse handleGCSUpload(@Context HttpServletRequest httpRequest) {
//        return service.handleGoogleCloudStorageUpload(httpRequest);
//    }

}
