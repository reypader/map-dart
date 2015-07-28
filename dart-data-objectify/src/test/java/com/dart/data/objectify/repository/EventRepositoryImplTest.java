package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.domain.Event;
import com.dart.data.repository.EventRepository;
import com.dart.data.objectify.domain.EventImpl;
import com.dart.data.objectify.domain.TestUser;
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
    private List<Event> testData = new ArrayList<>();
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
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime());
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
    }

    @Test
    public void testUpdate() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime());
        Event savedEvent = repo.add(event);
        savedEvent.setTitle("UPDATED TEST TITLE");
        savedEvent.setDescription("description");
        savedEvent.setStartDate(new Date());
        savedEvent.addImageURL("http://www.image.com/image.jpg");

        EventImpl updatedEvent = (EventImpl) repo.update(savedEvent);

        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedEvent, updatedEvent);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateEventBad() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        EventImpl event = new EventImpl(userKey, "TEST title", today.getTime(), tomorrow.getTime());

        repo.update(event);
    }

    @Test
    public void testRetrieve() throws Exception {
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), new Date());
        Event savedEvent = repo.add(event);

        Event foundEvent = repo.retrieve(Key.create(savedEvent).toWebSafeString());

        assertEverything(savedEvent, foundEvent);
    }

    @Test
    public void testDeleteEvent() throws Exception {
        EventImpl event = new EventImpl(userKey, "TEST title", new Date(), new Date());
        Event savedEvent = repo.add(event);

        repo.delete(savedEvent);

        int entityCount = ofy().load().type(EventImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void testFindEventsByUserBefore() throws Exception {
        TestUser user2 = new TestUser("username2");
        Key parent2 = ofy().save().entity(user2).now();
        Event event1 = new EventImpl(userKey, "Test title 1", new Date(), new Date());
        testData.add(repo.add(event1));
        Thread.sleep(2000);
        Date time = new Date();
        Event event2 = new EventImpl(userKey, "Test title 2", new Date(), new Date());
        Event event3 = new EventImpl(parent2, "Test title 3", new Date(), new Date());
        testData.add(repo.add(event2));
        testData.add(repo.add(event3));

        Collection<Event> result = repo.findEventsByUserBefore(user, time, QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(event2, result.iterator().next());
    }

    @Test
    public void testFindEventsCreatedBefore() throws Exception {
        Event event1 = new EventImpl(userKey, "Test title 1", new Date(), new Date());
        testData.add(repo.add(event1));
        Thread.sleep(2000);
        Date time = new Date();
        Event event2 = new EventImpl(userKey, "Test title 2", new Date(), new Date());
        testData.add(repo.add(event2));

        Collection<Event> result = repo.findEventsCreatedBefore(time, QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(event2, result.iterator().next());
    }

    @Test
    public void testFindEventsActiveOnBeforeTime() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR, 1);
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);
        EventImpl event1 = new EventImpl(userKey, "TEST title 1", today.getTime(), tomorrow.getTime());
        EventImpl event2 = new EventImpl(userKey, "TEST title 2", tomorrow.getTime(), nextWeek.getTime());
        testData.add(repo.add(event1));
        testData.add(repo.add(event2));

        Collection<Event> result = repo.findEventsActiveOn(later.getTime(), QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(event1, result.iterator().next());
    }


    @Test
    public void testFindEventsActiveOnAfterTime() throws Exception {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);
        EventImpl event1 = new EventImpl(userKey, "TEST title 1", today.getTime(), tomorrow.getTime());
        EventImpl event2 = new EventImpl(userKey, "TEST title 2", today.getTime(), nextWeek.getTime());
        testData.add(repo.add(event1));
        testData.add(repo.add(event2));

        Collection<Event> result = repo.findEventsActiveOn(tomorrow.getTime(), QUERY_LIMIT);

        assertEquals(1, result.size());
        assertEverything(event2, result.iterator().next());
    }

}