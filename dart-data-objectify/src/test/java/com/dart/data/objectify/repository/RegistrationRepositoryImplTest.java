package com.dart.data.objectify.repository;

import com.dart.common.test.TestDatastore;
import com.dart.data.domain.Registration;
import com.dart.data.exception.EntityNotFoundException;
import com.dart.data.objectify.domain.RegistrationImpl;
import com.dart.data.repository.RegistrationRepository;
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
public class RegistrationRepositoryImplTest {

    @Rule
    public TestDatastore store = new TestDatastore();
    private RegistrationRepository repo;

    private Closeable work;
    private List<Registration> testData = new ArrayList<>();

    @BeforeClass
    public static void refreshObjectify() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }

    @Before
    public void setUp() throws Exception {
        repo = new RegistrationRepositoryImpl();
        work = ObjectifyService.begin();
        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(0, entityCount);
    }

    @After
    public void tearDown() throws Exception {
        testData.clear();
        work.close();
    }

    private void assertEverything(Registration e1, Registration e2) {
        assertEquals(e1.getId(), e2.getId());
        assertEquals(e1.getDateCreated(), e2.getDateCreated());
        assertEquals(e1.getDisplayName(), e2.getDisplayName());
        assertEquals(e1.getEmail(), e2.getEmail());
        assertEquals(e1.getPassword(), e2.getPassword());
    }

    @Test
    public void testAdd() throws Exception {
        Registration registration = new RegistrationImpl("regCode", "email", "display name", "pass");

        Registration savedRegistration = repo.add(registration);

        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(1, entityCount);
        assertEquals("display name", savedRegistration.getDisplayName());
        assertEquals("regCode", savedRegistration.getId());
        assertEquals("email", savedRegistration.getEmail());
        assertEquals("pass", savedRegistration.getPassword());
        assertNotNull(savedRegistration.getDateCreated());
    }

    @Test
    public void testRetrieve() throws Exception {
        Registration registration = new RegistrationImpl("regCode", "email", "display name", "pass");

        Registration savedRegistration = repo.add(registration);

        Registration foundRegistration = repo.retrieve(savedRegistration.getId());

        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedRegistration, foundRegistration);
    }

    @Test
    public void testUpdate() throws Exception {
        Registration registration = new RegistrationImpl("regCode", "email", "display name", "pass");

        Registration savedRegistration = repo.add(registration);

        Registration updatedRegistration = repo.update(savedRegistration);

        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(1, entityCount);
        assertEverything(savedRegistration, updatedRegistration);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBad() throws Exception {
        Registration registration = new RegistrationImpl("regCode", "email", "display name", "pass");

        repo.update(registration);
    }


    @Test
    public void testDelete() throws Exception {
        Registration registration = new RegistrationImpl("regCode", "email", "display name", "pass");

        Registration savedRegistration = repo.add(registration);

        repo.delete(savedRegistration);

        int entityCount = ofy().load().type(RegistrationImpl.class).count();
        assertEquals(0, entityCount);
    }

}