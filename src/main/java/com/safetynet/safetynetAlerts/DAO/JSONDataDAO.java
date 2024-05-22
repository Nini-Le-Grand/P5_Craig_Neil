package com.safetynet.safetynetAlerts.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetAlerts.models.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Data
@Service
public class JSONDataDAO {

    private JSONData jsonData;

    public void setData(JSONDataDAO jsonDataDAO) {
        this.jsonData = jsonDataDAO.getJsonData();
    }

    public void loadDataFromFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.jsonData = objectMapper.readValue(new File(filePath), JSONData.class);
    }
}
