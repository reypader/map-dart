package com.dart.user.api.endpoint;

import com.dart.common.service.exception.IllegalTransactionException;
import com.dart.data.domain.User;
import com.dart.user.api.UpdateUserRequest;
import com.dart.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RMPader
 */
@RestController
@RequestMapping("/user")
public class UserEndpoint {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/update",
                    method = RequestMethod.POST)
    public void updateUser(UpdateUserRequest request,
                           @AuthenticationPrincipal User user) throws IllegalTransactionException {
        service.updateUser(request, user);
    }
}
