package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.User;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.domain.UserImpl;
import com.dart.data.repository.UserRepository;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.*;

/**
 * Created by RMPader on 7/27/15.
 */
public class UserRepositoryImplTest {

    @Rule
    public TestDatastore store = new TestDatastore();
    private UserRepository repo;

    private Closeable work;
    private List<User> testData = new ArrayList<>();

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new UserRepositoryImpl();
        work = ObjectifyService.begin();
        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() throws Exception {
        testData.clear();
        work.close();
    }

    private void assertEverything(User e1, User e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getDisplayName(), e2.getDisplayName());
        assertEquals(e1.getDescription(), e2.getDescription());
        assertEquals(e1.getPhotoURL(), e2.getPhotoURL());
        assertEquals(e1.getEmail(), e2.getEmail());
        assertEquals(DigestUtils.sha256Hex(e1.getEmail()), e2.getId());
    }

    @Test
    public void testAdd() throws Exception {
        User user = new UserImpl("username", "display name");
        user.setDescription("I like derping");
        user.setPhotoURL("URL");

        User savedUser = repo.add(user);

        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals("display name", savedUser.getDisplayName());
        assertEquals("I like derping", savedUser.getDescription());
        assertEquals("URL", savedUser.getPhotoURL());
        assertEquals("username", savedUser.getEmail());
        assertEquals(DigestUtils.sha256Hex("username"), savedUser.getId());
        assertNotNull(savedUser.getDateCreated());
    }

    @Test(expected = EntityCollisionException.class)
    public void testEntityCollision() throws Exception {
        User user = new UserImpl("username", "display name");
        User savedUser = repo.add(user);
        repo.add(savedUser);
    }

    @Test
    public void testRetrieve() throws Exception {
        User user = new UserImpl("username", "display name");
        User savedUser = repo.add(user);

        User foundUser = repo.retrieve(savedUser.getId());

        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedUser, foundUser);
    }

    @Test
    public void testRetrieveNull() throws Exception {
        assertNull(repo.retrieve("derp"));
    }

    @Test
    public void testUpdate() throws Exception {
        User user = new UserImpl("username", "display name");
        User savedUser = repo.add(user);
        savedUser.setDisplayName("New Name");

        User updatedUser = repo.update(savedUser);

        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedUser, updatedUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBad() throws Exception {
        User user = new UserImpl("username", "display name");

        repo.update(user);
    }


    @Test
    public void testDelete() throws Exception {
        User user = new UserImpl("username", "display name");
        User savedUser = repo.add(user);

        repo.delete(savedUser);

        Thread.sleep(1000);
        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void testRetrieveByEmail() throws Exception {
        User user = new UserImpl("username", "display name");
        User savedUser = repo.add(user);

        User foundUser = repo.retrieveByEmail("username");

        int entityCount = ofy().load().type(UserImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedUser, foundUser);
    }

    @Test(expected = NotFoundException.class)
    public void testRetrieveByEmailNull() throws Exception {
        User foundUser = repo.retrieveByEmail("wat");
        assertNull(foundUser);
    }
}