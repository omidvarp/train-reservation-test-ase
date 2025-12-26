package fr.univnantes.trainreservation;

import fr.univnantes.trainreservation.impl.TicketReservationSystemImpl;
import fr.univnantes.trainreservation.model.City;
import fr.univnantes.trainreservation.model.Train;
import fr.univnantes.trainreservation.model.Trip;
import fr.univnantes.trainreservation.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketReservationSystemTest {

    TicketReservationSystem system;
    City paris;
    City nantes;
    Train train;
    Trip trip;

    @BeforeEach
    void setUp() {
        system = new TicketReservationSystemImpl(ZoneId.of("Europe/Paris"));

        paris = system.createCity("Paris");
        nantes = system.createCity("Nantes");

        train = system.createTrain("TGV-001", 100);

        trip = system.createTrip(
                train,
                paris,
                nantes,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2)
        );
    }

    @Test
    void systemShouldBeCreatedSuccessfully() {
        assertNotNull(system);
    }

    @Test
    void cityShouldBeCreated() {
        assertEquals("Paris", paris.getName());
    }

    @Test
    void trainShouldBeCreatedWithCorrectCapacity() {
        assertEquals(100, train.getCapacity());
    }

    @Test
    void tripShouldBeCreatedSuccessfully() {
        assertNotNull(trip);
    }

    @Test
    void ticketShouldBeReservedSuccessfully() {
        Ticket ticket = system.bookTicket(trip);
        assertNotNull(ticket);
    }

    @Test
    void ticketShouldBeCancelledSuccessfully() {
        Ticket ticket = system.bookTicket(trip);
        system.cancelTicket(ticket);
        assertTrue(ticket.isCancelled());
    }

    @Test
    void ticketShouldBeChangedSuccessfully() {
        Ticket ticket = system.bookTicket(trip);
        Ticket newTicket = system.changeTicket(ticket, trip);
        assertNotNull(newTicket);
    }

    @Test
    void shouldFindTripsBetweenCities() {
        List<Trip> trips = system.findTrips(paris, nantes);
        assertFalse(trips.isEmpty());
    }

    @Test
    void tripShouldBeCancelled() {
        system.cancelTrip(trip);
        assertTrue(trip.isCancelled());
    }

    @Test
    void tripShouldBeDelayed() {
        system.delayTrip(trip, 30);
        assertTrue(trip.getDepartureTime().isAfter(
                LocalDateTime.now().plusDays(1)
        ));
    }
}

