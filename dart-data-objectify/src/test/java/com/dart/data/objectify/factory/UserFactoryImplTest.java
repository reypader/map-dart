package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.User;
import com.dart.data.factory.UserFactory;
import com.dart.data.objectify.domain.UserImpl;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

/**
 * Created by RMPader on 7/27/15.
 */
public class UserFactoryImplTest {

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
        ObjectifyService.register(UserImpl.class);
        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistableUser() throws Exception {
        UserFactory factory = new UserFactoryImpl();

        User user = factory.createUser("username", "Display Name");

        ofy().save().entity(user);
        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(1, entityCount);
    }
}