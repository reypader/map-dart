package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Session;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.domain.SessionImpl;
import com.dart.data.objectify.domain.TestUser;
import com.dart.data.repository.SessionRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.*;

/**
 * Created by RMPader on 7/27/15.
 */
public class SessionRepositoryImplTest {

    @Rule
    public TestDatastore store = new TestDatastore();
    private SessionRepository repo;

    private Closeable work;
    private Map<String, Session> testData = new HashMap<>();

    private TestUser user;
    private Key userKey;


    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new SessionRepositoryImpl();
        work = ObjectifyService.begin();
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(0, entityCount);

        user = new TestUser("username");
        userKey = ofy().save().entity(user).now();
    }

    @After
    public void tearDown() throws Exception {
        testData.clear();
        work.close();
    }

    private void assertEverything(Session e1, Session e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getUser(), e2.getUser());
        assertEquals(e1.getLocation(), e2.getLocation());
        assertEquals(e1.getBrowser(), e2.getBrowser());
        assertEquals(e1.getIPAddress(), e2.getIPAddress());
        assertEquals(e1.getDevice(), e2.getDevice());
        assertEquals(e1.getExpiry(), e2.getExpiry());
    }

    @Test
    public void testAdd() throws Exception {
        Date now = new Date();
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", now, "device", "browser", "location");

        Session savedSession = repo.add(session);

        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals("generated-token", savedSession.getId());
        assertEquals("location", savedSession.getLocation());
        assertEquals("browser", savedSession.getBrowser());
        assertEquals("device", savedSession.getDevice());
        assertEquals("127.0.0.1", savedSession.getIPAddress());
        assertEquals(now, savedSession.getExpiry());
        assertEquals(userKey, Key.create(savedSession.getUser()));
        assertNotNull(savedSession.getDateCreated());
    }

    @Test(expected = EntityCollisionException.class)
    public void testEntityCollision() throws Exception {
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", new Date(), "device", "browser", "location");

        Session savedSession = repo.add(session);
        repo.add(savedSession);
    }

    @Test
    public void testRetrieve() throws Exception {
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", new Date(), "device", "browser", "location");

        Session savedSession = repo.add(session);

        Session foundSession = repo.retrieve(savedSession.getId());

        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedSession, foundSession);
    }

    @Test
    public void testUpdate() throws Exception {
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", new Date(), "device", "browser", "location");

        Session savedSession = repo.add(session);

        Session updatedSession = repo.update(savedSession);

        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedSession, updatedSession);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBad() throws Exception {
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", new Date(), "device", "browser", "location");

        repo.update(session);
    }


    @Test
    public void testDelete() throws Exception {
        Session session = new SessionImpl("generated-token", userKey, "127.0.0.1", new Date(), "device", "browser", "location");

        Session savedSession = repo.add(session);

        repo.delete(savedSession);

        int entityCount = ofy().load().type(SessionImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void findSessionsOfUser() throws Exception {
        TestUser user2 = new TestUser("username2");
        Key userKey2 = ofy().save().entity(user2).now();

        Session session1 = new SessionImpl("generated-token1", userKey, "127.0.0.1", new Date(), "device", "browser", "location");
        Session session2 = new SessionImpl("generated-token2", userKey, "127.0.0.1", new Date(), "device", "browser", "location");
        Session session3 = new SessionImpl("generated-token3", userKey2, "127.0.0.1", new Date(), "device", "browser", "location");

        Session savedSession1 = repo.add(session1);
        Session savedSession2 = repo.add(session2);
        Session savedSession3 = repo.add(session3);

        testData.put(savedSession1.getId(), savedSession1);
        testData.put(savedSession2.getId(), savedSession2);
        testData.put(savedSession3.getId(), savedSession3);

        Collection<Session> result = repo.findSessionsOfUser(user);

        assertEquals(2, result.size());
        assertTrue(result.contains(savedSession1));
        assertTrue(result.contains(savedSession2));
        for (Session e : result) {
            assertEverything(testData.get(e.getId()), e);
        }
    }
}