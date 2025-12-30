package fr.univnantes.trainreservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketReservationSystemTest {

    private TicketReservationSystem system;
    private City nantes;
    private City paris;
    private Train train;
    private Trip trip;

    @BeforeEach
    void setUp() {
        system = new TicketReservationSystemImpl(ZoneId.systemDefault());

        nantes = system.addCity("Nantes");
        paris = system.addCity("Paris");

        train = system.addTrain("TGV-001", 100);

        trip = system.addTrip(
                train,
                nantes,
                paris,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2)
        );
    }

    // 1️⃣ رزرو موفق بلیط
    @Test
    void shouldReserveTicketSuccessfully() {
        Ticket ticket = system.bookTicket(trip);
        assertNotNull(ticket);
    }

    // 2️⃣ رزرو چند بلیط تا سقف ظرفیت
    @Test
    void shouldReserveMultipleTicketsWithinCapacity() {
        for (int i = 0; i < 100; i++) {
            assertNotNull(system.bookTicket(trip));
        }
    }

    // 3️⃣ رزرو بیش از ظرفیت (باید خطا دهد)
    @Test
    void shouldFailWhenTrainIsFull() {
        for (int i = 0; i < 100; i++) {
            system.bookTicket(trip);
        }
        assertThrows(IllegalStateException.class,
                () -> system.bookTicket(trip));
    }

    // 4️⃣ لغو بلیط
    @Test
    void shouldCancelTicketSuccessfully() {
        Ticket ticket = system.bookTicket(trip);
        system.cancelTicket(ticket);
        assertTrue(ticket.isCanceled());
    }

    // 5️⃣ تغییر بلیط
    @Test
    void shouldChangeTicketTrip() {
        City lyon = system.addCity("Lyon");
        Trip newTrip = system.addTrip(
                train,
                paris,
                lyon,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(3)
        );

        Ticket ticket = system.bookTicket(trip);
        system.changeTicket(ticket, newTrip);

        assertEquals(newTrip, ticket.getTrip());
    }

    // 6️⃣ جستجوی سفرهای موجود
    @Test
    void shouldFindAvailableTrips() {
        List<Trip> trips = system.findTrips(nantes, paris);
        assertFalse(trips.isEmpty());
    }

    // 7️⃣ لغو سفر
    @Test
    void shouldCancelTrip() {
        system.cancelTrip(trip);
        assertTrue(trip.isCanceled());
    }

    // 8️⃣ رزرو بلیط روی سفر لغوشده باید ناموفق باشد
    @Test
    void shouldNotAllowBookingCanceledTrip() {
        system.cancelTrip(trip);
        assertThrows(IllegalStateException.class,
                () -> system.bookTicket(trip));
    }

    // 9️⃣ تأخیر سفر
    @Test
    void shouldDelayTrip() {
        LocalDateTime newDeparture = trip.getDepartureTime().plusHours(1);
        system.delayTrip(trip, newDeparture);

        assertEquals(newDeparture, trip.getDepartureTime());
    }

    // 🔟 لغو بلیط نامعتبر
    @Test
    void shouldFailCancelingInvalidTicket() {
        Ticket fakeTicket = new Ticket(trip);
        assertThrows(IllegalArgumentException.class,
                () -> system.cancelTicket(fakeTicket));
    }
}
