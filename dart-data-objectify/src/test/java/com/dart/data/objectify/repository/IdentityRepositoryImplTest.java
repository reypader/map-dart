package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Identity;
import com.dart.data.exception.EntityCollisionException;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.domain.IdentityImpl;
import com.dart.data.objectify.domain.TestUser;
import com.dart.data.repository.IdentityRepository;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by RMPader on 7/27/15.
 */
public class IdentityRepositoryImplTest {

    @Rule
    public TestDatastore store = new TestDatastore();
    private IdentityRepository repo;

    private Closeable work;
    private List<Identity> testData = new ArrayList<>();

    private TestUser user;
    private Key userKey;

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new IdentityRepositoryImpl();
        work = ObjectifyService.begin();
        ObjectifyService.register(TestUser.class);
        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(0, entityCount);

        user = new TestUser("username");
        userKey = ofy().save().entity(user).now();
    }

    @After
    public void tearDown() throws Exception {
        testData.clear();
        work.close();
    }

    private void assertEverything(Identity e1, Identity e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getUser(), e2.getUser());
        assertEquals(e1.getProvidedIdentity(), e2.getProvidedIdentity());
        assertEquals(e1.getProvider(), e2.getProvider());
        assertEquals(e1.getData(),e2.getData());
    }

    @Test
    public void testAdd() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");
        identity.addData("test", "DATA");

        Identity savedIdentity = repo.add(identity);

        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals("self:id", savedIdentity.getId());
        assertEquals("self", savedIdentity.getProvider());
        assertEquals("id", savedIdentity.getProvidedIdentity());
        assertEquals(userKey, Key.create(savedIdentity.getUser()));
        assertEquals(identity.getData(), savedIdentity.getData());
        assertNotNull(savedIdentity.getDateCreated());
    }

    @Test(expected = EntityCollisionException.class)
    public void testEntityCollision() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");
        Identity savedEvent = repo.add(identity);
        repo.add(savedEvent);
    }

    @Test
    public void testRetrieve() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");

        Identity savedIdentity = repo.add(identity);

        Identity foundIdentity = repo.retrieve(savedIdentity.getId());

        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedIdentity, foundIdentity);
    }

    @Test
    public void testUpdate() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");

        Identity savedIdentity = repo.add(identity);

        Identity updatedIdentity = repo.update(savedIdentity);

        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedIdentity, updatedIdentity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBad() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");

        repo.update(identity);
    }


    @Test
    public void testDelete() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");

        Identity savedIdentity = repo.add(identity);

        repo.delete(savedIdentity);

        int entityCount = ofy().load().type(IdentityImpl.class).count();
        assertEquals(0, entityCount);
    }

    @Test
    public void findIdentityFromProvider() throws Exception {
        Identity identity = new IdentityImpl(userKey, "self", "id");
        identity.addData("test","DATA");
        Identity savedIdentity = repo.add(identity);

        Identity foundIdentity = repo.findIdentityFromProvider("id", "self");

        assertEverything(savedIdentity, foundIdentity);
    }
}