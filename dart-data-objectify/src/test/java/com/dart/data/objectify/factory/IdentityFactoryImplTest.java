package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Identity;
import com.dart.data.domain.User;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.objectify.domain.IdentityImpl;
import com.dart.data.objectify.domain.TestUser;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

/**
 * Created by RMPader on 7/27/15.
 */
public class IdentityFactoryImplTest {

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
        ObjectifyService.register(IdentityImpl.class);
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistableIdentity() throws Exception {
        IdentityFactory factory = new IdentityFactoryImpl();
        User user = new TestUser("username");
        Identity identity = factory.createIdentity(user, "Display Name", "derp");

        ofy().save().entity(identity);
        Thread.sleep(2000);
        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(1, entityCount);
    }
}