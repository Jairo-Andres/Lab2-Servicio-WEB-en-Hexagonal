package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}

	public void historial() {
		log.info("Into historial PersonaEntity in Input Adapter");
		personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void listarPersonas() {
		log.info("Listing all persons in CLI Adapter");
		List<PersonaModelCli> personas = personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		personas.forEach(System.out::println);
	}

	public void crearPersona(Integer cc, String nombre, String apellido, String genero, Integer edad) {
		log.info("Creating person in CLI Adapter");

		// Mapeo de género a Enum Gender
		Gender genderEnum;
		if (genero.equalsIgnoreCase("M")) {
			genderEnum = Gender.MALE;
		} else if (genero.equalsIgnoreCase("F")) {
			genderEnum = Gender.FEMALE;
		} else {
			throw new IllegalArgumentException("Género inválido: " + genero);
		}

		Person person = new Person(cc, nombre, apellido, genderEnum, edad, null, null);
		personInputPort.create(person);
		System.out.println("Persona creada: " + person);
	}

	public void actualizarPersona(Integer cc, String nombre, String apellido, String genero, Integer edad) {
		log.info("Updating person in CLI Adapter");
		try {
			// Ajusta el constructor de Person para coincidir con los parámetros
			// disponibles.
			Person person = new Person(); // Reemplaza con los parámetros correctos
			Person updatedPerson = personInputPort.edit(cc, person);
			System.out.println("Person updated: " + updatedPerson);
		} catch (NoExistException e) {
			System.out.println("Error updating person: " + e.getMessage());
		}
	}

	public void borrarPersona(Integer cc) {
		log.info("Deleting person in CLI Adapter");
		try {
			// Asegúrate de que el método correcto está siendo llamado según la interfaz.
			boolean deleted = personInputPort.drop(cc); // Cambia `delete` por `drop` si corresponde.
			if (deleted) {
				System.out.println("Person deleted successfully.");
			} else {
				System.out.println("Person not found.");
			}
		} catch (NoExistException e) {
			System.out.println("Error deleting person: " + e.getMessage());
		}
	}
}