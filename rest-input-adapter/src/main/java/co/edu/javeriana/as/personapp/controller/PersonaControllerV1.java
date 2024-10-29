package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;

	@Operation(summary = "Obtener lista de personas desde una base de datos")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
			@ApiResponse(responseCode = "404", description = "No se encontraron personas"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> personas(@PathVariable String database) {
		log.info("Into personas REST API");
		return personaInputAdapterRest.historial(database.toUpperCase());
	}

	@Operation(summary = "Crear una nueva persona")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Persona creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request) {
		log.info("Método crearPersona en el controller del API.");
		PersonaResponse response = personaInputAdapterRest.crearPersona(request);

		// Devolver un ResponseEntity con el objeto creado y el estado 201 (Created)
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
