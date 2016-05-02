package com.dart.user.service;

import com.dart.common.service.exception.IllegalTransactionException;
import com.dart.data.domain.User;
import com.dart.user.api.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public interface UserService {

    CheckEmailResponse checkEmailUsage(String email);

    void createRegistration(RegistrationRequest request);

    VerificationResponse verifyUser(String creationCode);

    RecaptchaResponse validateRecaptchaResult(RecaptchaRequest request, HttpServletRequest httpRequest);

    void updateUser(UpdateUserRequest request, User user) throws IllegalTransactionException;


}
