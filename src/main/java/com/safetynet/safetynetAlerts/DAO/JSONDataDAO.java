package com.safetynet.safetynetAlerts.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.models.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Getter
@Service
public class JSONDataDAO {

    private JSONData jsonData;

    public void loadDataFromFile() throws IOException {
        String filePath = "resources/data.json";
        ObjectMapper objectMapper = new ObjectMapper();
        this.jsonData = objectMapper.readValue(new File(filePath), JSONData.class);
    }
}
