package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;

@Mapper
public class EstudiosMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK(
				study.getProfession().getIdentification(),
				study.getPerson().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));

		// Solo se establece el ID de Persona para evitar recursión infinita
		PersonaEntity persona = new PersonaEntity();
		persona.setCc(study.getPerson().getIdentification());
		estudio.setPersona(persona);

		estudio.setProfesion(profesionMapperMaria.fromDomainToAdapter(study.getProfession()));
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study();
		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		// Solo establece el ID de la persona para evitar recursión infinita
		Person person = new Person();
		person.setIdentification(estudiosEntity.getPersona().getCc());
		study.setPerson(person);
		// Solo establece el ID de la profesión para evitar recursión infinita
		Profession profession = new Profession();
		profession.setIdentification(estudiosEntity.getProfesion().getId());
		study.setProfession(profession);
		return study;
	}

	private LocalDate validateGraduationDate(Date fecha) {
		if (fecha instanceof java.sql.Date) {
			return ((java.sql.Date) fecha).toLocalDate();
		}
		return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}
