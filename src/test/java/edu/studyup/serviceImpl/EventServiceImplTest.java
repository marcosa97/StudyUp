package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	/*
	 * Bug: Length name of 20 should not throw exception
	 */
	@Test
	void testUpdateEvent_WrongNameLength_goodCase() {
		int eventID = 1;
		String eventName = "01234567890123456789";
		Assertions.assertDoesNotThrow( () -> {
			eventServiceImpl.updateEventName(eventID, eventName);
		  });
	}
	
	@Test //new tests that an exception should be thrown if event name is longer than 20 chars
	void testUpdateEventName_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "this string is longer than 20 characters");
		  });
	}
	
	@Test
	void testGetActiveEvents_badCase() {
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("JohnSmith@email.com");
		student.setId(2);
				
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		Date pastDate = new Date(1, 1, 1);
		event.setDate(pastDate);	
		event.setName("Event 2");
		Location location = new Location(-122, 40);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
				
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Test that the new past event shouldn't be there
		assertFalse( eventServiceImpl.getActiveEvents().contains(event) );
	}
	
	@Test
	void testGetActiveEvents_goodCase() {
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("JohnSmith@email.com");
		student.setId(2);
				
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		Date futureDate = new Date(400, 1, 1);
		event.setDate(futureDate);	
		event.setName("Event 2");
		Location location = new Location(-122, 40);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
				
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Test that the new past event shouldn't be there
		assertTrue( eventServiceImpl.getActiveEvents().contains(event) );
	}
	
	@Test
	void testGetPastEvents_goodCase() {
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("JohnSmith@email.com");
		student.setId(2);
				
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		Date pastDate = new Date(1, 1, 1);
		event.setDate(pastDate);	
		event.setName("Event 2");
		Location location = new Location(-122, 40);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
				
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Test that the new past event shouldn't be there
		assertTrue( eventServiceImpl.getPastEvents().contains(event) );
	}
	
	@Test
	void testGetPastEvents_badCase() {
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("JohnSmith@email.com");
		student.setId(2);
				
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		Date futureDate = new Date(400, 1, 1);
		event.setDate(futureDate);	
		event.setName("Event 2");
		Location location = new Location(-122, 40);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
				
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Test that the new past event shouldn't be there
		assertFalse( eventServiceImpl.getPastEvents().contains(event) );
	}
	
	@Test
	void testAddStudentToEvent_goodCase() {
		//Create Student
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("JohnSmith@email.com");
		student.setId(2);
		
		Assertions.assertDoesNotThrow( () -> {
			eventServiceImpl.addStudentToEvent(student, eventID); 
		});
	}
	
	/*
	 * Bug: "students" should not have more than 2 elements in it
	 */
	@Test
	void testAddStudentToEvent_badCase() throws StudyUpException {
		//Create Student 2
		Student student2 = new Student();
		student2.setFirstName("John");
		student2.setLastName("Smith");
		student2.setEmail("JohnSmith@email.com");
		student2.setId(2);
		
		//Create Student 3
		Student student3 = new Student();
		student3.setFirstName("John");
		student3.setLastName("Smith");
		student3.setEmail("JohnSmith@email.com");
		student3.setId(2);
		
		eventServiceImpl.addStudentToEvent(student2, 1); 
		eventServiceImpl.addStudentToEvent(student3, 1);
		Event event = DataStorage.eventData.get(1);	
		
		assertTrue( event.getStudents().size() <= 2);
	}
	
	@Test
	void testAddStudentToEvent_nullEvent_badCase() {
		int eventID = 2;
		
		//Create Student 2
		Student student2 = new Student();
		student2.setFirstName("John");
		student2.setLastName("Smith");
		student2.setEmail("JohnSmith@email.com");
		student2.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student2, eventID);
		  });
	}
	
	@Test
	void testDeleteEvent_nullEvent_goodCase() {
		Event event = DataStorage.eventData.get(1);
		Event ret = eventServiceImpl.deleteEvent(1);
		
		assertEquals(event, ret);
	}
	
	@Test
	void testAddStudentToEvent_nullStudents() {
		Event event = DataStorage.eventData.get(1);
		Event ret = eventServiceImpl.deleteEvent(1);
		
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
				
		//Create Event1
		Event event2 = new Event();
		event2.setEventID(1);
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		Assertions.assertDoesNotThrow( () -> {
			eventServiceImpl.addStudentToEvent(student, event2.getEventID() );
		  });
	}
	
	/*
	@Test
	void testUpdateMethod_event_null_badcases() {
	    Event event = null;
	    Assertions.assertThrows(StudyUpException.class, () -> {
	    	eventServiceImpl.updateEvent(event);
	    });
	}
	*/
	
	
}
