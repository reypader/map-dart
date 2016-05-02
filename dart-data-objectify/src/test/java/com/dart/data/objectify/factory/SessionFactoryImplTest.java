package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.User;
import com.dart.data.factory.SessionFactory;
import com.dart.data.objectify.domain.SessionImpl;
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
public class SessionFactoryImplTest {

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
        ObjectifyService.register(SessionImpl.class);
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistableSession() throws Exception {
        SessionFactory factory = new SessionFactoryImpl();
        User user = new TestUser("username");
        SessionImpl session = (SessionImpl) factory.createSession();
        session.setUser(user);

        ofy().save().entity(session);
        Thread.sleep(2000);
        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(1, entityCount);
    }
}