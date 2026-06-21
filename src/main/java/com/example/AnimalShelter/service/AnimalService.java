package com.example.AnimalShelter.service;

import com.example.AnimalShelter.entity.AnimalEntity;
import com.example.AnimalShelter.entity.StatusEnum;
import com.example.AnimalShelter.repository.AnimalRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalEntity> getAll() {
        List<AnimalEntity> animal = animalRepository.findAll();
        sendToAudit(animal);
        return animal;
    }

    public AnimalEntity getById(long id) {
        AnimalEntity animal = animalRepository.findById(id);
        sendToAudit(animal);
        return animal;
    }

    public AnimalEntity post(AnimalEntity animalEntity) {
        AnimalEntity animal = animalRepository.save(animalEntity);
        sendToAudit(animal);
        return  animal;
    }

    public AnimalEntity put(AnimalEntity animalEntity) {
        AnimalEntity animal = animalRepository.update(animalEntity);
        sendToAudit(animal);
        return animal;
    }

    public AnimalEntity patch(long id, Map<String, Object> updates) {
        AnimalEntity animal = animalRepository.findById(id);

        if(updates.containsKey("status")) {
            animal.setStatus(StatusEnum.valueOf((String) updates.get("status")));
        }

        AnimalEntity updated = animalRepository.update(animal);
        sendToAudit(updated);
        return updated;
    }

    public void delete(long id) {
        animalRepository.delete(id);
        sendToAudit(id);
    }



    private void sendToAudit(Object request) {
        try {
            String json = objectMapper.writeValueAsString(
                    Map.of(
                            "id", UUID.randomUUID().toString(),
                            "request", objectMapper.writeValueAsString(request),
                            "createdAt", LocalDate.now().toString()
                    )
            );
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/audit"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
