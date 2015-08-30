package com.dart.photo.api.jersey;

import com.dart.common.service.aop.Authenticated;
import com.dart.common.service.http.UserPrincipal;
import com.dart.data.domain.User;
import com.dart.photo.api.CreateUploadURLResponse;
import com.dart.photo.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @Path("/upload/create")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Generate URL that can be used for uploading files",
            notes = "The URL can only be used to upload one file within 5 mins.",
            response = CreateUploadURLResponse.class)
    public CreateUploadURLResponse createUploadURL(@Context HttpServletRequest httpRequest, @QueryParam("md5") String md5, @QueryParam("type") String type) {
        UserPrincipal principal = (UserPrincipal) httpRequest.getUserPrincipal();
        User user = principal.getUser();
        return service.getUploadURL(md5, type, user.getId());
    }

}
