package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/study")
public class StudyController {

    @Autowired
    private StudyInputAdapterRest studyAdapterRest;

    @GetMapping("/{database}")
    public List<StudyResponse> getAllStudies(@PathVariable String database) {
        return studyAdapterRest.getAll(database);
    }

    @PostMapping
    public ResponseEntity<StudyResponse> createStudy(@RequestBody StudyRequest request) {
        StudyResponse response = studyAdapterRest.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{personId}/{professionId}")
    public ResponseEntity<StudyResponse> updateStudy(
            @PathVariable Integer personId,
            @PathVariable Integer professionId,
            @RequestBody StudyRequest request) {
        StudyResponse response = studyAdapterRest.update(personId, professionId, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{database}/{personId}/{professionId}")
    public ResponseEntity<Void> deleteStudy(
            @PathVariable String database,
            @PathVariable Integer personId,
            @PathVariable Integer professionId) {
        boolean deleted = studyAdapterRest.delete(personId, professionId, database);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
