package com.safetynet.safetynetAlerts;

import com.safetynet.safetynetAlerts.DAO.JSONDataDAO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetynetAlertsApplication {

    @Autowired
    public JSONDataDAO jsonDataDAO;

    private static final Logger logger = LoggerFactory.getLogger(SafetynetAlertsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SafetynetAlertsApplication.class, args);
    }

    @PostConstruct
    public void onStartup() {
        try {
            jsonDataDAO.loadDataFromFile();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
