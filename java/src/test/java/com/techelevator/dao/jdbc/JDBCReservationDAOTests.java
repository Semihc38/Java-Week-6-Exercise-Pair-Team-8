package com.techelevator.dao.jdbc;

import com.techelevator.dao.ReservationDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JDBCReservationDAOTests extends BaseDAOTests {

    private ReservationDAO dao;

    @Before
    public void setup() {
        dao = new JDBCReservationDAO(dataSource);
    }

    @Test
    public void createReservation_Should_ReturnNewReservationId() {
        int reservationCreated = dao.createReservation(1,
                "TEST NAME",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        assertEquals(reservationCreated, 45);
    }
    
    @Test
    public void getUpcomingReservation_ForPark() {
    	List<Reservation> reservations = dao.getUpcomingReservationsForPark(1);

        assertEquals(23,reservations.size());
    }

}
