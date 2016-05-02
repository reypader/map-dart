package com.dart.user.api.endpoint;

import com.dart.user.api.*;
import com.dart.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rpader on 4/27/16.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationEndpoint {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/email/check",
                    method = RequestMethod.GET)
    public CheckEmailResponse checkEmail(@RequestParam("email") String email) {
        return service.checkEmailUsage(email);
    }

    @RequestMapping(value = "/register",
                    method = RequestMethod.POST)
    public void register(RegistrationRequest request) {
        service.createRegistration(request);
    }

    @RequestMapping(value = "/verify",
                    method = RequestMethod.GET)
    public VerificationResponse verify(@RequestParam("creation_code") String creationCode) {
        return service.verifyUser(creationCode);
    }

    @RequestMapping(value = "/recaptcha",
                    method = RequestMethod.POST)
    public RecaptchaResponse recaptcha(HttpServletRequest httpRequest, RecaptchaRequest request) {
        return service.validateRecaptchaResult(request, httpRequest);
    }


}
