package com.dart.event.service;

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
import com.dart.event.api.CreateEventRequest;
import com.dart.event.api.CreateEventResponse;
import com.dart.event.api.FindEventResponse;
import com.dart.event.api.Location;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by RMPader on 7/28/15.
 */
public class EventServiceTest {

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

        EventService service = new EventService(factorySpy, eventRepoSpy, userRepoSpy);

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

        EventService service = new EventService(factorySpy, eventRepoSpy, userRepoSpy);

        FindEventResponse actualResponse = service.findEvent(event.getId());

        verify(eventRepoSpy, times(1)).retrieve(event.getId());
        assertFields(event, actualResponse);
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
//        EventService service = new EventService(factorySpy, eventRepoSpy, userRepoSpy);
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
//        EventService service = new EventService(factorySpy, eventRepoSpy, userRepoSpy);
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