package co.edu.javeriana.as.personapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.ProfessionInputAdapterRest;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Profession", description = "Operations related to Profession")
@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionController {

    @Autowired
    private ProfessionInputAdapterRest professionAdapterRest;

    @Operation(summary = "Get all professions")
    @GetMapping("/{database}")
    public List<ProfessionResponse> getAllProfessions(@PathVariable String database) {
        return professionAdapterRest.getAll(database);
    }

    @Operation(summary = "Create a new profession")
    @PostMapping
    public ResponseEntity<ProfessionResponse> createProfession(@RequestBody ProfessionRequest request) {
        ProfessionResponse created = professionAdapterRest.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a profession")
    @PutMapping("/{database}/{id}")
    public ResponseEntity<ProfessionResponse> updateProfession(
            @PathVariable Integer id,
            @PathVariable String database,
            @RequestBody ProfessionRequest request) {
        request.setDatabase(database);
        ProfessionResponse updated = professionAdapterRest.update(id, request);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a profession")
    @DeleteMapping("/{database}/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable Integer id, @PathVariable String database) {
        boolean deleted = professionAdapterRest.delete(id, database);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
