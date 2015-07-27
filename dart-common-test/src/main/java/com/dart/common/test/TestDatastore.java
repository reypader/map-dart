package com.dart.common.test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.rules.ExternalResource;


public class TestDatastore extends ExternalResource {

    private LocalServiceTestHelper helper;

    @Override
    protected void before() throws Throwable {
        helper = new LocalServiceTestHelper(getDataStoreConfig());
        helper.setUp();
    }

    public LocalServiceTestConfig getDataStoreConfig() {
        return new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy();
    }

    @Override
    protected void after() {
        helper.tearDown();
    }

}
