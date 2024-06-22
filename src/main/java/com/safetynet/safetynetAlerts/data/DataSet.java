package com.safetynet.safetynetAlerts.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.safetynet.safetynetAlerts.models.JSONData;

import java.io.File;
import java.io.IOException;

/**
 * Service for loading JSON data from a file into the data set.
 */
@Getter
@Service
public class DataSet {

    private JSONData jsonData;

    private static final Logger logger = LoggerFactory.getLogger(DataSet.class);

    /**
     * Loads JSON data from a file and maps it to Java objects.
     *
     * @throws IOException If an I/O error occurs while reading the file or parsing the JSON data.
     */
    public void loadDataFromFile() throws IOException {
        logger.info("Loading data from file");
        try {
            String filePath = "resources/data.json";
            ObjectMapper objectMapper = new ObjectMapper();
            this.jsonData = objectMapper.readValue(new File(filePath), com.safetynet.safetynetAlerts.models.JSONData.class);
            logger.info("Data loaded to data set");
        } catch (IOException e) {
            logger.error("Error while loading data from file {}", e.getMessage());
            throw new IOException(e);
        }
    }
}
