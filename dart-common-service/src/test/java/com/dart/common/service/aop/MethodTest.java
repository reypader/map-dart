package com.dart.common.service.aop;

import com.dart.data.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RMPader
 */
public interface MethodTest {
    public User test(Object trash, User user, Object trash2, HttpServletRequest request);
}
