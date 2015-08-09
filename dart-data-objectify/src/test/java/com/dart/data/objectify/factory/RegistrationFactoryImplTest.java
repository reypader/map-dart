package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Registration;
import com.dart.data.factory.RegistrationFactory;
import com.dart.data.objectify.domain.RegistrationImpl;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

/**
 * Created by RMPader on 7/27/15.
 */
public class RegistrationFactoryImplTest {

    @Rule
    public TestDatastore store = new TestDatastore();
    private Closeable work;

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        work = ObjectifyService.begin();
        ObjectifyService.register(RegistrationImpl.class);
        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistableRegistration() throws Exception {
        RegistrationFactory factory = new RegistrationFactoryImpl();

        Registration registration = factory.createRegistration("username", "Display Name", "derp");

        ofy().save().entity(registration);
        Thread.sleep(2000);
        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(1, entityCount);
    }
}