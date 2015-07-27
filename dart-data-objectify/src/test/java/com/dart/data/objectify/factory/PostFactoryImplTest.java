package com.dart.data.objectify.factory;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Event;
import com.dart.data.domain.Post;
import com.dart.data.domain.User;
import com.dart.data.factory.PostFactory;
import com.dart.data.objectify.domain.PostImpl;
import com.dart.data.objectify.domain.TestEvent;
import com.dart.data.objectify.domain.TestUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;

/**
 * Created by RMPader on 7/27/15.
 */
public class PostFactoryImplTest {

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
        ObjectifyService.register(PostImpl.class);
        ObjectifyService.register(TestUser.class);
        ObjectifyService.register(TestEvent.class);
        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() {
        work.close();
    }

    @Test
    public void testPersistablePost() throws Exception {
        PostFactory factory = new PostFactoryImpl();
        User user = new TestUser("username");
        Key userKey = ofy().save().entity(user).now();

        Event event = new TestEvent(userKey);
        ofy().save().entity(event).now();

        Post post = factory.createPost(event, user, "content");

        ofy().save().entity(post);
        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(1, entityCount);
    }
}