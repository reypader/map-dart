package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.factory.EventFactory;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.objectify.domain.TestUser;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

/**
 * Created by RMPader on 7/27/15.
 */
public class EventFactoryImplTest {

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
        ObjectifyService.register(EventImpl.class);
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistableEvent() throws Exception {
        EventFactory factory = new EventFactoryImpl();
        User user = new TestUser("username");
        Event event = factory.createEvent(user,"title",new Date(),new Date());

        ofy().save().entity(event);
        Thread.sleep(2000);
        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(1, entityCount);
    }
}