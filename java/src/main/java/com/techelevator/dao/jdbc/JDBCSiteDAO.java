package com.techelevator.dao.jdbc;

import com.techelevator.dao.SiteDAO;
import com.techelevator.model.Site;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

public class JDBCSiteDAO implements SiteDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCSiteDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        
    	String query = "SELECT s.*, p.park_id FROM site s JOIN campground c ON s.camground_id = c.campground_id JOIN park p ON c.park_id = p.park_id WHERE max_rv_length != 0 AND p.park_id = ?";
    	SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, parkId);
    	
    	List<Site> siteList = new ArrayList<>();
    	
	while(rowSet.next()) {
			
			Site site = mapRowToSite(rowSet);
			siteList.add(site);
		}
		
		return siteList;
    	
    	
    }
    
    
    public List<Site> getAvailableSites() {
    	
    	String query = "SELECT s.* FROM site s LEFT JOIN reservation r ON r.site_id = s.site_id WHERE r.reservation_id IS NULL";
    	
    	SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);
    	
    	List<Site> siteList = new ArrayList<>();
    	
    	while(rowSet.next()) {
			
			Site site = mapRowToSite(rowSet);
			siteList.add(site);
		}
		
		return siteList;
    	
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
