package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;

	// ===================== PERSONA =====================

	@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
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

	@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
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
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
	@PutMapping(path = "/{database}/{dni}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> actualizarPersona(
			@PathVariable String database,
			@PathVariable String dni,
			@RequestBody PersonaRequest request) {

		log.info("Método actualizarPersona en el controller del API.");
		request.setDni(dni); // Asegurar que el DNI en el request coincide con el de la URL
		PersonaResponse response = personaInputAdapterRest.actualizarPersona(request, database);

		return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
	}

	@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
	@DeleteMapping(path = "/{database}/{dni}")
	public ResponseEntity<Void> eliminarPersona(
			@PathVariable String database,
			@PathVariable String dni) {

		log.info("Método eliminarPersona en el controller del API.");
		try {
			personaInputAdapterRest.eliminarPersona(dni, database);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
