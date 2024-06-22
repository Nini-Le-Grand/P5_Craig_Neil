package com.safetynet.safetynetAlerts;

import com.safetynet.safetynetAlerts.data.DataSet;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * This class represents the main entry point of the Safetynet Alerts application.
 * It is responsible for initializing and starting up the application.
 * <p>
 * The application is designed to load JSON data from a file using a provided {@code DataSet} instance
 * upon startup, to facilitate data retrieval and manipulation.
 * <p>
 * This class utilizes Spring Boot framework to enable easier application configuration and deployment.
 *
 * @see DataSet
 */
@SpringBootApplication
public class SafetynetAlertsApplication {

    /**
     * The data access object responsible for loading JSON data.
     */
    @Autowired
    public DataSet dataSet;

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SafetynetAlertsApplication.class, args);
    }

    /**
     * Method invoked after the application context has been initialized.
     * It loads data from a JSON file using the injected {@code DataSet} instance.
     * <p>
     *
     * @throws IOException if an I/O error occurs while loading data from the file.
     *
     * In case of any exception during data loading, the error is thrown and application run fails.
     */
    @PostConstruct
    public void onStartup() throws IOException {
        dataSet.loadDataFromFile();
    }
}
