package com.safetynet.safetynetAlerts;

import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetynetAlertsApplication {

	@Autowired
	public JSONDataDAO jsonDataDAO;

	public static void main(String[] args) {
		SpringApplication.run(SafetynetAlertsApplication.class, args);
	}

	@PostConstruct
	public void onStartup() {
		try {
			jsonDataDAO.loadDataFromFile("resources/data.json");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
