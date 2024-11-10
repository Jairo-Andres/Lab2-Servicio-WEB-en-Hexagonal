package co.edu.javeriana.as.personapp.mongo.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;

@Mapper
public class PersonaMapperMongo {

	@Autowired
	@Lazy
	private EstudiosMapperMongo estudiosMapperMongo;

	@Autowired
	@Lazy
	private TelefonoMapperMongo telefonoMapperMongo;

	public PersonaDocument fromDomainToAdapter(Person person) {
		PersonaDocument personaDocument = new PersonaDocument();
		personaDocument.setId(person.getIdentification());
		personaDocument.setNombre(person.getFirstName());
		personaDocument.setApellido(person.getLastName());
		personaDocument.setGenero(validateGenero(person.getGender()));
		personaDocument.setEdad(validateEdad(person.getAge()));
		personaDocument.setEstudios(validateEstudios(person.getStudies()));
		personaDocument.setTelefonos(validateTelefonos(person.getPhoneNumbers()));
		return personaDocument;
	}

	private String validateGenero(Gender gender) {
		if (gender == null) {
			return " "; // Retorna un espacio en blanco o un valor por defecto para evitar
						// `NullPointerException`
		}
		return gender == Gender.FEMALE ? "F" : gender == Gender.MALE ? "M" : " ";
	}

	private Integer validateEdad(Integer age) {
		return age != null && age >= 0 ? age : null;
	}

	private List<EstudiosDocument> validateEstudios(List<Study> studies) {
		return studies != null && !studies.isEmpty()
				? studies.stream()
						.map(study -> estudiosMapperMongo.fromDomainToAdapter(study))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	private List<TelefonoDocument> validateTelefonos(List<Phone> phoneNumbers) {
		return phoneNumbers != null && !phoneNumbers.isEmpty() ? phoneNumbers.stream()
				.map(phone -> telefonoMapperMongo.fromDomainToAdapter(phone)).collect(Collectors.toList())
				: new ArrayList<>();
	}

	public Person fromAdapterToDomain(PersonaDocument personaDocument) {
		Person person = new Person();
		person.setIdentification(personaDocument.getId());
		person.setFirstName(personaDocument.getNombre());
		person.setLastName(personaDocument.getApellido());
		person.setGender(validateGender(personaDocument.getGenero()));
		person.setAge(validateAge(personaDocument.getEdad()));
		person.setStudies(validateStudies(personaDocument.getEstudios()));
		person.setPhoneNumbers(validatePhones(personaDocument.getTelefonos()));
		return person;
	}

	private @NonNull Gender validateGender(String genero) {
		if (genero == null || genero.trim().isEmpty()) {
			return Gender.OTHER; // Retorna un valor por defecto para evitar `NullPointerException`
		}
		return "F".equals(genero) ? Gender.FEMALE : "M".equals(genero) ? Gender.MALE : Gender.OTHER;
	}

	private Integer validateAge(Integer edad) {
		return edad != null && edad >= 0 ? edad : null;
	}

	private List<Study> validateStudies(List<EstudiosDocument> estudiosDocuments) {
		return estudiosDocuments != null && !estudiosDocuments.isEmpty()
				? estudiosDocuments.stream()
						.map(estudio -> {
							Study study = new Study();
							study.setGraduationDate(estudio.getFecha());
							study.setUniversityName(estudio.getUniver());
							// Solo establece el ID de la persona para evitar recursión infinita
							Person minimalPerson = new Person();
							minimalPerson.setIdentification(estudio.getPrimaryPersona().getId());
							study.setPerson(minimalPerson);
							// Solo establece el ID de la profesión para evitar recursión infinita
							Profession minimalProfession = new Profession();
							minimalProfession.setIdentification(estudio.getPrimaryProfesion().getId());
							study.setProfession(minimalProfession);
							return study;
						})
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	private List<Phone> validatePhones(List<TelefonoDocument> telefonosDocuments) {
		return telefonosDocuments != null && !telefonosDocuments.isEmpty()
				? telefonosDocuments.stream()
						.map(telefono -> {
							Phone phone = telefonoMapperMongo.fromAdapterToDomain(telefono);
							// Solo establece el ID del propietario para evitar recursión
							Person minimalPerson = new Person();
							minimalPerson.setIdentification(telefono.getPrimaryDuenio().getId());
							phone.setOwner(minimalPerson);
							return phone;
						})
						.collect(Collectors.toList())
				: new ArrayList<>();
	}
}
