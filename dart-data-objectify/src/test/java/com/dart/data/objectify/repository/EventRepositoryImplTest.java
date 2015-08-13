package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Event;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.objectify.domain.TestUser;
import com.dart.data.repository.EventRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.*;

/**
 * Created by RMPader on 7/25/15.
 */
public class EventRepositoryImplTest {

    private static final int QUERY_LIMIT = 10;

    @Rule
    public TestDatastore store = new TestDatastore();
    private EventRepository repo;

    private Closeable work;
    private Map<String, Event> testData = new HashMap<>();
    private TestUser user;
    private Key userKey;

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new EventRepositoryImpl();
        work = ObjectifyService.begin();
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(0, entityCount);

        user = new TestUser("username");
        userKey = ofy().save().entity(user).now();
    }

    @After
    public void tearDown() {
        testData.clear();
        work.close();
    }

    private void assertEverything(Event e1, Event e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getOrganizer(), e2.getOrganizer());
        assertEquals(e1.getStartDate(), e2.getStartDate());
        assertEquals(e1.getEndDate(), e2.getEndDate());
        assertEquals(e1.getTitle(), e2.getTitle());
        assertEquals(e1.getDescription(), e2.getDescription());
        assertEquals(e1.getImageURLs(), e2.getImageURLs());
    }

    @Test
    public void testAdd() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime(), new Point(1, 1));
        event.setDescription("describe");
        event.addImageURL("URL1");
        event.addImageURL("URL2");

        EventImpl savedEvent = (EventImpl) repo.add(event);

        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals(today.getTime(), savedEvent.getStartDate());
        assertEquals(tomorrow.getTime(), savedEvent.getEndDate());
        assertEquals("TEST title", savedEvent.getTitle());
        assertEquals("describe", savedEvent.getDescription());
        assertEquals(2, savedEvent.getImageURLs().size());
        assertTrue(savedEvent.getImageURLs().contains("URL1"));
        assertTrue(savedEvent.getImageURLs().contains("URL2"));
        assertEquals(Key.create(savedEvent).toWebSafeString(), savedEvent.getId());
        assertNotNull(savedEvent.getDateCreated());
        assertEquals(userKey, Key.create(savedEvent.getOrganizer()));
        assertFalse(savedEvent.isFinished());
    }

    @Test(expected = EntityCollisionException.class)
    public void testEntityCollision() throws Exception {
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), new Date(), new Point(1, 1));
        Event savedEvent = repo.add(event);
        repo.add(savedEvent);
    }

    @Test
    public void testUpdate() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime(), new Point(1, 1));
        Event savedEvent = repo.add(event);
        savedEvent.setTitle("UPDATED TEST TITLE");
        savedEvent.setDescription("description");
        savedEvent.setStartDate(new Date());
        savedEvent.addImageURL("http://www.image.com/image.jpg");
        savedEvent.setEndDate(today.getTime());
        EventImpl updatedEvent = (EventImpl) repo.update(savedEvent);

        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedEvent, updatedEvent);
        EventImpl doubleCheck = (EventImpl) ofy().load().key(Key.create(savedEvent)).now();
        assertTrue(doubleCheck.isFinished());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateEventBad() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime(), new Point(1, 1));

        repo.update(event);
    }

    @Test
    public void testRetrieve() throws Exception {
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), new Date(), new Point(1, 1));
        Event savedEvent = repo.add(event);

        Event foundEvent = repo.retrieve(Key.create(savedEvent).toWebSafeString());

        assertEverything(savedEvent, foundEvent);
    }

    @Test
    public void testRetrieveNull() throws Exception {
        assertNull(repo.retrieve(Key.create(EventImpl.class, 1234L).toWebSafeString()));
    }

    @Test
    public void testDelayedRetrieve() throws Exception {
        Calendar later = Calendar.getInstance();
        later.add(Calendar.SECOND, 3);
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), later.getTime(), new Point(1, 1));
        Event savedEvent = repo.add(event);

        Thread.sleep(5000);
        Event foundEvent = repo.retrieve(Key.create(savedEvent).toWebSafeString());

        assertEverything(savedEvent, foundEvent);
        EventImpl doubleCheck = (EventImpl) ofy().load().key(Key.create(savedEvent)).now();
        assertTrue(doubleCheck.isFinished());
    }

    @Test
    public void testDelete() throws Exception {
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), new Date(), new Point(1, 1));
        Event savedEvent = repo.add(event);

        repo.delete(savedEvent);

        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void testFindUnfinishedEventsInAreaRectangle() throws Exception {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = new EventImpl(userKey, "Test title 1", yesterday.getTime(), yesterday.getTime(), new Point(121.040777f, 14.360065f));
        Event event2 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(121.048857f, 14.361015f));
        Event event3 = new EventImpl(userKey, "Test title 3", yesterday.getTime(), tomorrow.getTime(), new Point(121.817784f, 14.520892f));
        Event event4 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(121.040777f, 14.360065f));
        event1 = repo.add(event1);
        event2 = repo.add(event2);
        event3 = repo.add(event3);
        event4 = repo.add(event4);
        testData.put(event1.getId(), event1);
        testData.put(event2.getId(), event2);
        testData.put(event3.getId(), event3);
        testData.put(event4.getId(), event4);

        Point ne = new Point(121.050403f, 14.368595f);
        Point sw = new Point(121.034380f, 14.357199f);
        Rectangle area = new Rectangle(ne, sw);
        Collection<Event> result = repo.findUnfinishedEventsInArea(area);

        assertEquals(2, result.size());
        assertTrue(result.contains(event2));
        assertTrue(result.contains(event4));
        for (Event e : result) {
            assertEverything(testData.get(e.getId()), e);
        }
    }

    @Test
    public void testFindUnfinishedEventsInAreaCircle() throws Exception {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = new EventImpl(userKey, "Test title 1", yesterday.getTime(), yesterday.getTime(), new Point(121.040777f, 14.360065f));
        Event event2 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(121.048857f, 14.361015f));
        Event event3 = new EventImpl(userKey, "Test title 3", yesterday.getTime(), tomorrow.getTime(), new Point(121.817784f, 14.520892f));
        Event event4 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(121.040777f, 14.360065f));
        event1 = repo.add(event1);
        event2 = repo.add(event2);
        event3 = repo.add(event3);
        event4 = repo.add(event4);
        testData.put(event1.getId(), event1);
        testData.put(event2.getId(), event2);
        testData.put(event3.getId(), event3);
        testData.put(event4.getId(), event4);

        Point center = new Point(121.043041f, 14.359820f);
        Collection<Event> result = repo.findUnfinishedEventsInArea(center, 6000);

        assertEquals(2, result.size());
        assertTrue(result.contains(event2));
        assertTrue(result.contains(event4));
        for (Event e : result) {
            assertEverything(testData.get(e.getId()), e);
        }
    }

    @Test
    public void testFindUnfinishedEventsByUser() throws Exception {
        TestUser user2 = new TestUser("username2");
        Key parent2 = ofy().save().entity(user2).now();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = new EventImpl(userKey, "Test title 1", yesterday.getTime(), yesterday.getTime(), new Point(1, 1));
        Event event2 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(1, 1));
        Event event3 = new EventImpl(parent2, "Test title 3", yesterday.getTime(), yesterday.getTime(), new Point(1, 1));
        event1 = repo.add(event1);
        event2 = repo.add(event2);
        event3 = repo.add(event3);
        testData.put(event1.getId(), event1);
        testData.put(event2.getId(), event2);
        testData.put(event3.getId(), event3);

        Collection<Event> result = repo.findUnfinishedEventsByUser(user);

        assertEquals(1, result.size());
        assertEverything(event2, result.iterator().next());
    }

    @Test
    public void testFindFinishedEventsByUser() throws Exception {
        TestUser user2 = new TestUser("username2");
        Key parent2 = ofy().save().entity(user2).now();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = new EventImpl(userKey, "Test title 1", yesterday.getTime(), yesterday.getTime(), new Point(1, 1));
        Event event2 = new EventImpl(userKey, "Test title 2", yesterday.getTime(), tomorrow.getTime(), new Point(1, 1));
        Event event3 = new EventImpl(parent2, "Test title 3", yesterday.getTime(), yesterday.getTime(), new Point(1, 1));
        event1 = repo.add(event1);
        event2 = repo.add(event2);
        event3 = repo.add(event3);
        testData.put(event1.getId(), event1);
        testData.put(event2.getId(), event2);
        testData.put(event3.getId(), event3);
        Collection<Event> result = repo.findFinishedEventsByUser(user, new Date(), QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(event1, result.iterator().next());
    }

}