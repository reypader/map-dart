package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.domain.Event;
import com.dart.data.objectify.domain.TestEvent;
import com.dart.data.objectify.domain.TestUser;
import com.dart.data.objectify.domain.PostImpl;
import com.dart.data.domain.Post;
import com.dart.data.repository.PostRepository;
import com.dart.data.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.*;

/**
 * Created by RMPader on 7/27/15.
 */
public class PostRepositoryImplTest {

    private static final int QUERY_LIMIT = 10;

    @Rule
    public TestDatastore store = new TestDatastore();
    private PostRepository repo;

    private Closeable work;
    private List<Post> testData = new ArrayList<>();
    private User user;
    private Key userKey;
    private Event event;
    private Key eventKey;

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new PostRepositoryImpl();
        work = ObjectifyService.begin();
        ObjectifyService.register(TestUser.class);
        ObjectifyService.register(TestEvent.class);
        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(0, entityCount);

        user = new TestUser("username");
        userKey = ofy().save().entity(user).now();

        event = new TestEvent(userKey);
        eventKey = ofy().save().entity(event).now();
    }

    @After
    public void tearDown() throws Exception {
        testData.clear();
        work.close();
    }

    private void assertEverything(Post e1, Post e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getEvent(), e2.getEvent());
        assertEquals(e1.getUser(), e2.getUser());
        assertEquals(e1.getContent(), e2.getContent());
        assertEquals(e1.getImageURLs(), e2.getImageURLs());
    }

    @Test
    public void testAdd() throws Exception {
        String content = "This is a test content";
        Post post = new PostImpl(eventKey, userKey, content);
        post.addImageURL("URL1");
        post.addImageURL("URL2");

        Post savedPost = repo.add(post);

        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals(content, savedPost.getContent());
        assertEquals(2, savedPost.getImageURLs().size());
        assertTrue(savedPost.getImageURLs().contains("URL1"));
        assertTrue(savedPost.getImageURLs().contains("URL2"));
        assertEquals(Key.create(savedPost).toWebSafeString(), savedPost.getId());
        assertEquals(userKey, Key.create(savedPost.getUser()));
        assertEquals(eventKey, Key.create(savedPost.getEvent()));
        assertNotNull(savedPost.getDateCreated());
    }

    @Test
    public void testRetrieve() throws Exception {
        String content = "This is a test content";
        Post post = new PostImpl(eventKey, userKey, content);
        Post savedPost = repo.add(post);

        Post foundPost = repo.retrieve(savedPost.getId());

        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedPost, foundPost);
    }

    @Test
    public void testUpdate() throws Exception {
        String content = "This is a test content";
        Post post = new PostImpl(eventKey, userKey, content);
        Post savedPost = repo.add(post);
        savedPost.addImageURL("URL1");
        savedPost.addImageURL("URL2");

        Post updatedPost = repo.update(savedPost);

        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedPost, updatedPost);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBad() throws Exception {
        String content = "This is a test content";
        Post post = new PostImpl(eventKey, userKey, content);

        repo.update(post);
    }


    @Test
    public void testDelete() throws Exception {
        String content = "This is a test content";
        Post post = new PostImpl(eventKey, userKey, content);
        Post savedPost = repo.add(post);

        repo.delete(savedPost);

        int entityCount = ofy().load().type(PostImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void findPostsByUserBefore() throws Exception {
        TestUser user2 = new TestUser("username2");
        Key userKey2 = ofy().save().entity(user2).now();
        String content = "This is a test content";
        Post post1 = new PostImpl(eventKey, userKey, content);
        testData.add(repo.add(post1));
        Thread.sleep(2000);
        Date time = new Date();
        Post post2 = new PostImpl(eventKey, userKey2, content);
        Post post3 = new PostImpl(eventKey, userKey, content);
        testData.add(repo.add(post2));
        testData.add(repo.add(post3));

        Collection<Post> result = repo.findPostsByUserBefore(user2, time, QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(post2, result.iterator().next());
    }

    @Test
    public void findPostsByEventBefore() throws Exception {
        Event event2 = new TestEvent(userKey);
        Key eventKey2 = ofy().save().entity(event2).now();
        String content = "This is a test content";
        Post post1 = new PostImpl(eventKey, userKey, content);
        testData.add(repo.add(post1));
        Thread.sleep(2000);
        Date time = new Date();
        Post post2 = new PostImpl(eventKey2, userKey, content);
        Post post3 = new PostImpl(eventKey, userKey, content);
        testData.add(repo.add(post2));
        testData.add(repo.add(post3));

        Collection<Post> result = repo.findPostsByEventBefore(event2, time, QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(post2, result.iterator().next());
    }
}