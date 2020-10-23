package com.techelevator.dao.jdbc;

import com.techelevator.dao.ReservationDAO;
import com.techelevator.model.Reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCReservationDAO implements ReservationDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCReservationDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        
    	String query = "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?)";
    	Integer nextId = getNextReservationId();
    	
    	jdbcTemplate.update(query, nextId, siteId, name, fromDate, toDate, LocalDate.now());
    	
    	
    	return nextId;
    }
    
    public List<Reservation> getUpcomingReservationsForPark (int parkId) {
    	
    	String query = "SELECT r.*, p.park_id FROM reservation r JOIN site s ON r.site_id = s.site_id JOIN campground c ON s.campground_id = c.campground_id JOIN park p ON c.park_id = p.park_id WHERE from_date >= current_date AND from_date <= current_date + 30 AND p.park_id = ?";
    	SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, parkId);
    	
    	List<Reservation> reservationList = new ArrayList<>();
    	
    	while(rowSet.next()) {
			
			Reservation reservation = mapRowToReservation(rowSet);
			reservationList.add(reservation);
		}
		
		return reservationList;
    	
    }
    
    

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }
    
	private Integer getNextReservationId() {
		
		String query = "SELECT nextval('reservation_reservation_id_seq')";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);
		if(rowSet.next()) {
			
			return rowSet.getInt(1);
		}
		return null;
	}


}
