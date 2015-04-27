package org.john.iplookup;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class MaxDBDAO {
	private DatabaseReader cityReader;
	
	private static MaxDBDAO instance = new MaxDBDAO();
	
	public void init(File databaseCity) {
		try {
			cityReader = new DatabaseReader.Builder(databaseCity).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static MaxDBDAO getInstance() {
		return instance;
	}

	public CityResponse getLocation(String ip) {
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			return  cityReader.city(ipAddress);
		} catch (IOException | GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
