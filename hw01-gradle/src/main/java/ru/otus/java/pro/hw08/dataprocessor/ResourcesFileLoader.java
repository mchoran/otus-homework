package ru.otus.java.pro.hw08.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.java.pro.hw08.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileProcessException("Файл не найден в resources: " + fileName);
            }
            JsonNode root = objectMapper.readTree(inputStream);
            List<Measurement> measurements = new ArrayList<>();
            for (JsonNode node : root) {
                measurements.add(new Measurement(
                        node.get("name").asText(),
                        node.get("value").asDouble()
                ));
            }
            return measurements;
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
