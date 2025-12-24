package fr.univnantes.trainreservation;

import fr.univnantes.trainreservation.impl.TicketReservationSystemImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class TicketReservationSystemTest {

    TicketReservationSystem system;

    @BeforeEach
    void setUp() {
        system = new TicketReservationSystemImpl(ZoneId.of("Europe/Paris"));
    }

    @Test
    void systemShouldBeCreatedSuccessfully() {
        assertNotNull(system);
    }
}
