package com.sallyf.sallyf;

import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.EventDispatcher.EventHandlerInterface;
import com.sallyf.sallyf.EventDispatcher.EventInterface;
import com.sallyf.sallyf.EventDispatcher.EventType;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

class TestEvent implements EventInterface
{

}

public class EventDispatcherTest
{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void registerTest()
    {
        EventDispatcher eventDispatcher = new EventDispatcher();

        EventType<TestEvent> testEventType = new EventType<>("test.test_event");

        eventDispatcher.register(testEventType, (eventType, testEvent) -> {});
        eventDispatcher.register(testEventType, (eventType) -> {});

        HashMap<EventType, ArrayList<EventHandlerInterface>> events = eventDispatcher.getEvents();

        assertEquals(1, events.size());
        assertEquals(2, events.get(testEventType).size());
    }

    @Test
    public void dispatchTest()
    {
        System.setOut(new PrintStream(outContent));

        EventDispatcher eventDispatcher = new EventDispatcher();

        EventType<TestEvent> testEventType = new EventType<>("test.test_event");

        eventDispatcher.register(testEventType, (eventType) -> {
            System.out.print("#1#");
        });

        eventDispatcher.register(testEventType, (eventType, testEvent) -> {
            System.out.print("#2#");
        });

        eventDispatcher.register(testEventType, (eventType, testEvent) -> {});
        eventDispatcher.register(testEventType, (eventType, testEvent) -> {});

        eventDispatcher.dispatch(testEventType);

        assertEquals("#1##2#", outContent.toString());
    }
}
