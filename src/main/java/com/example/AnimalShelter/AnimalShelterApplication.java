package com.example.AnimalShelter;


import com.azure.storage.blob.BlobContainerClient;
import com.example.AnimalShelter.entity.AnimalEntity;
import com.example.AnimalShelter.entity.GenderEnum;
import com.example.AnimalShelter.entity.StatusEnum;
import com.example.AnimalShelter.repository.AnimalRepository;
import com.example.AnimalShelter.service.AnimalService;
import com.example.AnimalShelter.service.AzureBlobService;
import config.ConnectAzure;
import config.ConnectDb;
import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class AnimalShelterApplication {
    public static void main(String[] args) throws SQLException {

        var dsl = ConnectDb.getDSL();
        AnimalRepository repository = new AnimalRepository(dsl);
        BlobContainerClient containerClient = ConnectAzure.getContainerClient();

        AzureBlobService azureBlobService = new AzureBlobService(containerClient);
        AnimalService service = new AnimalService(repository, azureBlobService);

        Javalin app = Javalin.create().start(8080);

        app.get("/animals", ctx -> ctx.json(service.getAll()));

        app.get("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(service.getById(id));
        });

        app.post("/animals", ctx -> {
            AnimalEntity animal = new AnimalEntity();
            animal.setName(ctx.formParam("name"));
            animal.setType(ctx.formParam("type"));
            animal.setGender(GenderEnum.valueOf(ctx.formParam("gender")));
            animal.setStatus(StatusEnum.valueOf(ctx.formParam("status")));

            byte[] imageBytes = null;
            String fileName = null;

            var uploadedFile = ctx.uploadedFile("image");
            if (uploadedFile != null) {
                imageBytes = uploadedFile.content().readAllBytes();
                fileName = UUID.randomUUID() + "_" + uploadedFile.filename();
            }

            ctx.json(service.post(animal, imageBytes, fileName));
        });

        app.put("/animals/{id}", ctx -> {
            AnimalEntity animal = ctx.bodyAsClass(AnimalEntity.class);
            ctx.json(service.put(animal));
        });

        app.patch("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Object> update = ctx.bodyAsClass(Map.class);
            ctx.json(service.patch(id, update));
        });

        app.delete("/animals/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            service.delete(id);
            ctx.result("Deleted");
        });

        app.get("/animals/{id}/image", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            byte[] imageBytes = service.getImage(id);
            ctx.contentType("image/jpeg");
            ctx.json(imageBytes);
        });
    }
}
