package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Mapper
public class PersonaMapperMaria {

	@Autowired
	@Lazy
	private EstudiosMapperMaria estudiosMapperMaria;

	@Autowired
	private TelefonoMapperMaria telefonoMapperMaria;

	public PersonaEntity fromDomainToAdapter(Person person) {
		PersonaEntity personaEntity = new PersonaEntity();
		personaEntity.setCc(person.getIdentification());
		personaEntity.setNombre(person.getFirstName());
		personaEntity.setApellido(person.getLastName());
		personaEntity.setGenero(validateGenero(person.getGender()));
		personaEntity.setEdad(validateEdad(person.getAge()));
		personaEntity.setEstudios(validateEstudios(person.getStudies()));
		personaEntity.setTelefonos(validateTelefonos(person.getPhoneNumbers()));
		return personaEntity;
	}

	private Character validateGenero(Gender gender) {
		if (gender == null) {
			// Manejo explícito del caso nulo, podrías lanzar una excepción si es necesario.
			throw new IllegalArgumentException("El campo 'gender' no puede ser nulo");
		}
		return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : ' ';
	}

	private Integer validateEdad(Integer age) {
		return age != null && age >= 0 ? age : null;
	}

	private List<EstudiosEntity> validateEstudios(List<Study> studies) {
		return studies != null && !studies.isEmpty()
				? studies.stream()
						.map(study -> estudiosMapperMaria.fromDomainToAdapter(study))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	private List<TelefonoEntity> validateTelefonos(List<Phone> phoneNumbers) {
		return phoneNumbers != null && !phoneNumbers.isEmpty() ? phoneNumbers.stream()
				.map(phone -> telefonoMapperMaria.fromDomainToAdapter(phone)).collect(Collectors.toList())
				: new ArrayList<TelefonoEntity>();
	}

	public Person fromAdapterToDomain(PersonaEntity personaEntity) {
		Person person = new Person();
		person.setIdentification(personaEntity.getCc());
		person.setFirstName(personaEntity.getNombre());
		person.setLastName(personaEntity.getApellido());
		person.setGender(validateGender(personaEntity.getGenero()));
		person.setAge(validateAge(personaEntity.getEdad()));
		person.setStudies(validateStudies(personaEntity.getEstudios()));
		person.setPhoneNumbers(validatePhones(personaEntity.getTelefonos()));
		return person;
	}

	private @NonNull Gender validateGender(Character genero) {
		return genero == 'F' ? Gender.FEMALE : genero == 'M' ? Gender.MALE : Gender.OTHER;
	}

	private Integer validateAge(Integer edad) {
		return edad != null && edad >= 0 ? edad : null;
	}

	private List<Study> validateStudies(List<EstudiosEntity> estudiosEntity) {
		return estudiosEntity != null && !estudiosEntity.isEmpty()
				? estudiosEntity.stream()
						.map(estudio -> {
							Study study = estudiosMapperMaria.fromAdapterToDomain(estudio);
							// Solo establece el ID para evitar recursión
							Person minimalPerson = new Person();
							minimalPerson.setIdentification(estudio.getPersona().getCc());
							study.setPerson(minimalPerson);
							return study;
						})
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	private List<Phone> validatePhones(List<TelefonoEntity> telefonoEntities) {
		return telefonoEntities != null && !telefonoEntities.isEmpty()
				? telefonoEntities.stream()
						.map(telefono -> {
							Phone phone = telefonoMapperMaria.fromAdapterToDomain(telefono);
							// Solo establece el ID del propietario para evitar recursión
							Person minimalPerson = new Person();
							minimalPerson.setIdentification(telefono.getDuenio().getCc());
							phone.setOwner(minimalPerson);
							return phone;
						})
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

}