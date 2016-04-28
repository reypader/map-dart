package com.dart.common.service.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author RMPader
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {

}
