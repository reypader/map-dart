package com.dart.data.domain;

import java.util.Map;

/**
 * Identity entity interface that provides necessary methods for performing business logic.
 *
 * @author RMPader
 */
public interface Identity extends Entity {

    User getUser();

    String getProvider();

    String getProvidedIdentity();

    Map<String, Object> getData();

    void addData(String key, Object value);

}
