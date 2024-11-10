package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.PhoneInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/phone")
public class PhoneController {

    @Autowired
    private PhoneInputAdapterRest phoneInputAdapterRest;

    @GetMapping("/{database}")
    public List<PhoneResponse> getAllPhones(@PathVariable String database) {
        return phoneInputAdapterRest.getAllPhones(database);
    }

    @PostMapping
    public ResponseEntity<PhoneResponse> createPhone(@RequestBody PhoneRequest request) {
        PhoneResponse response = phoneInputAdapterRest.createPhone(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{number}")
    public ResponseEntity<PhoneResponse> updatePhone(@PathVariable String number, @RequestBody PhoneRequest request)
            throws NoExistException {
        PhoneResponse response = phoneInputAdapterRest.updatePhone(number, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{number}/{database}")
    public ResponseEntity<Void> deletePhone(@PathVariable String number, @PathVariable String database) {
        boolean deleted = phoneInputAdapterRest.deletePhone(number, database);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
