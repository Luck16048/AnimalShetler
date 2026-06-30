package com.example.AnimalShelter;


import com.azure.storage.blob.BlobContainerClient;
import com.example.AnimalShelter.entity.AnimalEntity;
import com.example.AnimalShelter.repository.AnimalRepository;
import com.example.AnimalShelter.service.AnimalService;
import com.example.AnimalShelter.service.AzureBlobService;
import config.ConnectAzure;
import config.ConnectDb;
import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.Map;

public class AnimalShelterApplication {
    public static void main(String[] args)  throws SQLException {

        var dsl = ConnectDb.getDSL();
        AnimalRepository repository = new AnimalRepository(dsl);
        AnimalService service = new AnimalService(repository);

        BlobContainerClient containerClient = ConnectAzure.getContainerClient();
        AzureBlobService azureBlobService = new AzureBlobService(containerClient);

        Javalin app = Javalin.create().start(8080);

        app.get("/animals", ctx -> ctx.json(service.getAll()));

        app.get("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(service.getById(id));
        });

        app.post("/animals", ctx -> {
            AnimalEntity animal = ctx.bodyAsClass(AnimalEntity.class);
            ctx.json(service.post((animal)));
        });

        app.put("/animals/{id}", ctx -> {
            AnimalEntity animal = ctx.bodyAsClass(AnimalEntity.class);
            ctx.json(service.put(animal));
        });

        app.patch("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Object> update= ctx.bodyAsClass(Map.class);
            ctx.json(service.patch(id,update));
        });

        app.delete("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            service.delete(id);
            ctx.result("Deleted");
        });
    }
}
