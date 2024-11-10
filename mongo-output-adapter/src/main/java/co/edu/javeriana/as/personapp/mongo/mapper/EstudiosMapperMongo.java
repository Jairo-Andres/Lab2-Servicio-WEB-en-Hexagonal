package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMongo {

	@Autowired
	@Lazy
	private PersonaMapperMongo personaMapperMongo;

	@Autowired
	@Lazy
	private ProfesionMapperMongo profesionMapperMongo;

	public EstudiosDocument fromDomainToAdapter(Study study) {
		EstudiosDocument estudio = new EstudiosDocument();
		estudio.setId(validateId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
		estudio.setPrimaryPersona(validatePrimaryPersona(study.getPerson()));
		estudio.setPrimaryProfesion(validatePrimaryProfesion(study.getProfession()));
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private String validateId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
		return identificationPerson + "-" + identificationProfession;
	}

	private PersonaDocument validatePrimaryPersona(@NonNull Person person) {
		return personaMapperMongo.fromDomainToAdapter(person);
	}

	private ProfesionDocument validatePrimaryProfesion(@NonNull Profession profession) {
		return profesionMapperMongo.fromDomainToAdapter(profession);
	}

	private LocalDate validateFecha(LocalDate graduationDate) {
		return graduationDate != null ? graduationDate : null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study();
		// Solo establece el ID de la persona para evitar recursión infinita
		Person person = new Person();
		person.setIdentification(estudiosDocument.getPrimaryPersona().getId());
		study.setPerson(person);
		// Solo establece el ID de la profesión para evitar recursión infinita
		Profession profession = new Profession();
		profession.setIdentification(estudiosDocument.getPrimaryProfesion().getId());
		study.setProfession(profession);
		study.setGraduationDate(estudiosDocument.getFecha());
		study.setUniversityName(estudiosDocument.getUniver());
		return study;
	}
}
