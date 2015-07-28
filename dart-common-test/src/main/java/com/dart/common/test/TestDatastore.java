package com.dart.common.test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.rules.ExternalResource;

/**
 * <p>
 * Wrapped {@link LocalServiceTestHelper} as JUnit {@link ExternalResource} Rule.
 * This is to help testers by automatically setting up the local Datastore with
 * strong consistency before each test case. The Datastore is also torn down after each test.
 * </p>
 * <p>
 * <strong>NOTE:</strong> It may still be necessary to place a Thread.sleep() when testing persist operations.
 * </p>
 */
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
