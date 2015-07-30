package com.dart.event.service;

import com.dart.common.test.domain.DummyEvent;
import com.dart.common.test.factory.DummyEventFactory;
import com.dart.common.test.factory.DummyUserFactory;
import com.dart.common.test.repository.DummyEventRepository;
import com.dart.common.test.repository.DummyUserRepository;
import com.dart.data.domain.Event;
import com.dart.data.domain.User;
import com.dart.data.factory.EventFactory;
import com.dart.data.repository.EventRepository;
import com.dart.data.repository.UserRepository;
import com.dart.data.util.Point;
import com.dart.data.util.Rectangle;
import com.dart.event.api.CreateEventRequest;
import com.dart.event.api.CreateEventResponse;
import com.dart.event.api.FindEventResponse;
import com.dart.event.api.Location;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by RMPader on 7/28/15.
 */
public class EventServiceImplTest {

    private DummyUserRepository dummyUserRepo = new DummyUserRepository();
    private DummyEventRepository dummyEventRepo = new DummyEventRepository();
    private DummyEventFactory dummyEventFactory = new DummyEventFactory();
    private DummyUserFactory dummyUserFactory = new DummyUserFactory();

    @After
    public void tearDown() {
        dummyEventRepo.getStoredData().clear();
        dummyUserRepo.getStoredData().clear();
    }

    private void assertFields(Event event, FindEventResponse actualResponse) {
        assertEquals(event.getId(), actualResponse.getId());
        assertEquals(event.getTitle(), actualResponse.getTitle());
        assertEquals(event.getDescription(), actualResponse.getDescription());
        assertEquals(event.getOrganizer().getId(), actualResponse.getOrganizer().getId());
        assertEquals(event.getOrganizer().getDisplayName(), actualResponse.getOrganizer().getName());
        assertEquals(event.getEndDate(), actualResponse.getEndDate());
        assertEquals(event.getStartDate(), actualResponse.getStartDate());
        assertEquals(event.getLocation().getLatitude(), actualResponse.getLocation().getLatitude(), 0.0000005f);
        assertEquals(event.getLocation().getLongitude(), actualResponse.getLocation().getLongitude(), 0.0000005f);
        assertArrayEquals(event.getImageURLs().toArray(), actualResponse.getImageURLs());
    }

    @Test
    public void testCreateEvent() {
        User organizer = dummyUserFactory.createUser("username", "display name");
        dummyUserRepo.add(organizer);

        float lon = 121.032990f;
        float lat = 14.557514f;
        Location location = new Location();
        location.setLongitude(lon);
        location.setLatitude(lat);

        Point point = new Point(lon, lat);

        CreateEventRequest request = new CreateEventRequest();
        request.setOrganizerId(organizer.getId());
        request.setTitle("Test Title");
        request.setDescription("This is a description");
        request.setStartDate(new Date());
        request.setEndDate(new Date());
        request.setImageURLs(new String[]{"URL1", "URL2"});
        request.setLocation(location);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);

        CreateEventResponse actualResponse = service.createEvent(request);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);

        verify(userRepoSpy, times(1)).retrieve(request.getOrganizerId());
        verify(factorySpy, times(1)).createEvent(organizer, request.getTitle(), request.getStartDate(), request.getEndDate(), point);
        verify(eventRepoSpy, times(1)).add(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();
        assertEquals(request.getTitle(), capturedEvent.getTitle());
        assertEquals(request.getDescription(), capturedEvent.getDescription());
        assertArrayEquals(request.getImageURLs(), capturedEvent.getImageURLs().toArray());
        assertEquals(request.getEndDate(), capturedEvent.getEndDate());
        assertEquals(request.getStartDate(), capturedEvent.getStartDate());
        assertEquals(request.getOrganizerId(), capturedEvent.getOrganizer().getId());
        assertNotNull(actualResponse.getEventId());
    }

    @Test
    public void testFindEvent() {
        User organizer = dummyUserFactory.createUser("username", "display name");
        dummyUserRepo.add(organizer);

        float lon = 121.032990f;
        float lat = 14.557514f;
        Point point = new Point(lon, lat);

        Event event = dummyEventFactory.createEvent(organizer, "This is a title", new Date(), new Date(), point);
        event.setDescription("This is a description");
        event.addImageURL("URL1");
        event.addImageURL("URL2");
        event = dummyEventRepo.add(event);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);

        FindEventResponse actualResponse = service.findEvent(event.getId());

        verify(eventRepoSpy, times(1)).retrieve(event.getId());
        assertFields(event, actualResponse);
    }

    @Test
    public void testFindEventsRectangle() throws Exception {
        User organizer = dummyUserFactory.createUser("username", "display name");
        dummyUserRepo.add(organizer);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), yesterday.getTime(), new Point(121.040777f, 14.360065f));
        Event event2 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.048857f, 14.361015f));
        Event event3 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.817784f, 14.520892f));
        Event event4 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.040777f, 14.360065f));
        dummyEventRepo.add(event1);
        dummyEventRepo.add(event2);
        dummyEventRepo.add(event3);
        dummyEventRepo.add(event4);

        Point ne = new Point(121.050403f, 14.368595f);
        Point sw = new Point(121.034380f, 14.357199f);
        Rectangle area = new Rectangle(ne, sw);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);

        Collection<FindEventResponse> actualResponse = service.findEvents(area);

        verify(eventRepoSpy, times(1)).findUnfinishedEventsInArea(area);
        assertEquals(2, actualResponse.size());

        List<Event> actual = new ArrayList<>();
        for (FindEventResponse response : actualResponse) {
            Event a = dummyEventRepo.getStoredData().get(response.getId());
            assertFields(a, response);
            actual.add(a);
        }
        assertTrue(actual.contains(event2));
        assertTrue(actual.contains(event4));
    }

    @Test
    public void testFindEventsCircle() throws Exception {

        User organizer = dummyUserFactory.createUser("username", "display name");
        dummyUserRepo.add(organizer);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Event event1 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), yesterday.getTime(), new Point(121.040777f, 14.360065f));
        Event event2 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.048857f, 14.361015f));
        Event event3 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.817784f, 14.520892f));
        Event event4 = dummyEventFactory.createEvent(organizer, "This is a title", yesterday.getTime(), tomorrow.getTime(), new Point(121.040777f, 14.360065f));
        dummyEventRepo.add(event1);
        dummyEventRepo.add(event2);
        dummyEventRepo.add(event3);
        dummyEventRepo.add(event4);

        Point center = new Point(121.043041f, 14.359820f);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);

        Collection<FindEventResponse> actualResponse = service.findEvents(center, 6000);

        verify(eventRepoSpy, times(1)).findUnfinishedEventsInArea(center, 6000);
        assertEquals(2, actualResponse.size());

        List<Event> actual = new ArrayList<>();
        for (FindEventResponse response : actualResponse) {
            Event a = dummyEventRepo.getStoredData().get(response.getId());
            assertFields(a, response);
            actual.add(a);
        }
        assertTrue(actual.contains(event2));
        assertTrue(actual.contains(event4));
    }

    @Test
    public void testFindEventsOfUser() throws Exception {
        User organizer1 = dummyUserFactory.createUser("username1", "display name");
        dummyUserRepo.add(organizer1);
        User organizer2 = dummyUserFactory.createUser("username2", "display name too");
        dummyUserRepo.add(organizer2);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);
        Calendar now = Calendar.getInstance();

        float lon = 121.032990f;
        float lat = 14.557514f;
        Point point = new Point(lon, lat);

        DummyEvent event1 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title", yesterday.getTime(), yesterday.getTime(), point);
        dummyEventRepo.add(event1);
        DummyEvent event2 = (DummyEvent) dummyEventFactory.createEvent(organizer2, "This is a title too", tomorrow.getTime(), tomorrow.getTime(), point);
        dummyEventRepo.add(event2);
        DummyEvent event3 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title, promise", lastMonth.getTime(), yesterday.getTime(), point);
        dummyEventRepo.add(event3);
        DummyEvent event4 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a nevermind", now.getTime(), tomorrow.getTime(), point);
        dummyEventRepo.add(event4);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);

        List<FindEventResponse> actualResponse = service.findFinishedEventsOfUserBefore(organizer1.getId(), now.getTime(), 10);

        verify(userRepoSpy, times(1)).retrieve(organizer1.getId());
        verify(eventRepoSpy, times(1)).findFinishedEventsByUser(organizer1, now.getTime(), 10);
        assertEquals(2, actualResponse.size());
        List<String> expectedIds = new ArrayList<>();
        expectedIds.add(event3.getId());
        expectedIds.add(event1.getId());
        for (FindEventResponse response : actualResponse) {
            assertTrue(expectedIds.contains(response.getId()));
            expectedIds.remove(response.getId());
        }
        for (FindEventResponse response : actualResponse) {
            Event event = dummyEventRepo.getStoredData().get(response.getId());
            assertFields(event, response);
        }
    }

    @Test
    public void testFindEventsOfUserBefore() throws Exception {
        User organizer1 = dummyUserFactory.createUser("username1", "display name");
        dummyUserRepo.add(organizer1);
        User organizer2 = dummyUserFactory.createUser("username2", "display name too");
        dummyUserRepo.add(organizer2);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);


        float lon = 121.032990f;
        float lat = 14.557514f;
        Point point = new Point(lon, lat);

        DummyEvent event1 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title", yesterday.getTime(), yesterday.getTime(), point);
        dummyEventRepo.add(event1);
        DummyEvent event2 = (DummyEvent) dummyEventFactory.createEvent(organizer2, "This is a title too", tomorrow.getTime(), tomorrow.getTime(), point);
        dummyEventRepo.add(event2);
        DummyEvent event3 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title, promise", lastMonth.getTime(), yesterday.getTime(), point);
        dummyEventRepo.add(event3);
        DummyEvent event4 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a nevermind", new Date(), tomorrow.getTime(), point);
        dummyEventRepo.add(event4);

        UserRepository userRepoSpy = spy(dummyUserRepo);
        EventRepository eventRepoSpy = spy(dummyEventRepo);
        EventFactory factorySpy = spy(dummyEventFactory);

        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);


        List<FindEventResponse> actualResponse = service.findEventsOfUser(organizer1.getId(), 10);

        verify(userRepoSpy, times(1)).retrieve(organizer1.getId());
        verify(eventRepoSpy, times(1)).findFinishedEventsByUser(eq(organizer1), isA(Date.class), eq(10));
        verify(eventRepoSpy, times(1)).findUnfinishedEventsByUser(organizer1);
        assertEquals(3, actualResponse.size());
        List<String> expectedIds = new ArrayList<>();
        expectedIds.add(event3.getId());
        expectedIds.add(event4.getId());
        expectedIds.add(event1.getId());
        for (FindEventResponse response : actualResponse) {
            assertTrue(expectedIds.contains(response.getId()));
            expectedIds.remove(response.getId());
        }
        for (FindEventResponse response : actualResponse) {
            Event event = dummyEventRepo.getStoredData().get(response.getId());
            assertFields(event, response);
        }
    }

//    @Test
//    public void testGetEventsOfUser() {
//        User organizer1 = dummyUserFactory.createUser("username1", "display name");
//        dummyUserRepo.add(organizer1);
//        User organizer2 = dummyUserFactory.createUser("username2", "display name too");
//        dummyUserRepo.add(organizer2);
//
//        Calendar lastMonth = Calendar.getInstance();
//        lastMonth.add(Calendar.MONTH, -1);
//
//        DummyEvent event1 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title", new Date(), new Date());
//        event1.setDescription("This is a description");
//        event1.addImageURL("URL1");
//        event1.addImageURL("URL2");
//        event1.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event1);
//
//        DummyEvent event2 = (DummyEvent) dummyEventFactory.createEvent(organizer2, "This is a title too", new Date(), new Date());
//        event2.setDescription("This is a description too");
//        event2.addImageURL("URL4");
//        event2.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event2);
//
//        DummyEvent event3 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a title, promise", new Date(), new Date());
//        event3.setDescription("This is ridiculous");
//        event3.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event3);
//
//        DummyEvent event4 = (DummyEvent) dummyEventFactory.createEvent(organizer1, "This is a nevermind", new Date(), new Date());
//        event4.setDescription("Screw this");
//        dummyEventRepo.add(event4);
//
//        UserRepository userRepoSpy = spy(dummyUserRepo);
//        EventRepository eventRepoSpy = spy(dummyEventRepo);
//        EventFactory factorySpy = spy(dummyEventFactory);
//
//        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);
//
//        Calendar yesterday = Calendar.getInstance();
//        yesterday.add(Calendar.DAY_OF_YEAR, -1);
//
//        List<FindEventResponse> actualResponse = service.getEventsOfUserBefore(organizer1.getId(), yesterday.getTime(), 10);
//
//        verify(userRepoSpy, times(1)).retrieve(organizer1.getId());
//        verify(eventRepoSpy, times(1)).findEventsByUserBefore(organizer1, yesterday.getTime(), 10);
//        assertEquals(2, actualResponse.size());
//        List<String> expectedIds = new ArrayList<>();
//        expectedIds.add(event3.getId());
//        expectedIds.add(event1.getId());
//        for (FindEventResponse response : actualResponse) {
//            assertTrue(expectedIds.contains(response.getId()));
//            expectedIds.remove(response.getId());
//        }
//        for (FindEventResponse response : actualResponse) {
//            Event event = dummyEventRepo.getStoredData().get(response.getId());
//            assertFields(event, response);
//        }
//    }
//
//    @Test
//    public void testGetEventsBefore() {
//        Calendar lastMonth = Calendar.getInstance();
//        lastMonth.add(Calendar.MONTH, -1);
//
//        DummyEvent event1 = (DummyEvent) dummyEventFactory.createEvent(new DummyUser(), "This is a title", new Date(), new Date());
//        event1.setDescription("This is a description");
//        event1.addImageURL("URL1");
//        event1.addImageURL("URL2");
//        event1.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event1);
//
//        DummyEvent event2 = (DummyEvent) dummyEventFactory.createEvent(new DummyUser(), "This is a title too", new Date(), new Date());
//        event2.setDescription("This is a description too");
//        event2.addImageURL("URL4");
//        event2.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event2);
//
//        DummyEvent event3 = (DummyEvent) dummyEventFactory.createEvent(new DummyUser(), "This is a title, promise", new Date(), new Date());
//        event3.setDescription("This is ridiculous");
//        event3.setDateCreated(lastMonth.getTime());
//        dummyEventRepo.add(event3);
//
//        DummyEvent event4 = (DummyEvent) dummyEventFactory.createEvent(new DummyUser(), "This is a nevermind", new Date(), new Date());
//        event4.setDescription("Screw this");
//        dummyEventRepo.add(event4);
//
//        UserRepository userRepoSpy = spy(dummyUserRepo);
//        EventRepository eventRepoSpy = spy(dummyEventRepo);
//        EventFactory factorySpy = spy(dummyEventFactory);
//
//        EventServiceImpl service = new EventServiceImpl(factorySpy, eventRepoSpy, userRepoSpy);
//
//        Calendar yesterday = Calendar.getInstance();
//        yesterday.add(Calendar.DAY_OF_YEAR, -1);
//
//        List<FindEventResponse> actualResponse = service.getEventsBefore(yesterday.getTime(), 10);
//
//        verify(eventRepoSpy, times(1)).findEventsCreatedBefore(yesterday.getTime(), 10);
//        assertEquals(3, actualResponse.size());
//        List<String> expectedIds = new ArrayList<>();
//        expectedIds.add(event3.getId());
//        expectedIds.add(event2.getId());
//        expectedIds.add(event1.getId());
//        for (FindEventResponse response : actualResponse) {
//            assertTrue(expectedIds.contains(response.getId()));
//            expectedIds.remove(response.getId());
//        }
//        for (FindEventResponse response : actualResponse) {
//            Event event = dummyEventRepo.getStoredData().get(response.getId());
//            assertFields(event, response);
//        }
//    }


}