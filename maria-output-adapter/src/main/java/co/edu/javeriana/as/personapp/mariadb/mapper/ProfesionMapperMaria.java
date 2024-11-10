package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

@Mapper
public class ProfesionMapperMaria {

	@Autowired
	private EstudiosMapperMaria estudiosMapperMaria;

	public ProfesionEntity fromDomainToAdapter(Profession profession) {
		ProfesionEntity profesionEntity = new ProfesionEntity();
		profesionEntity.setId(profession.getIdentification());
		profesionEntity.setNom(profession.getName());
		profesionEntity.setDes(validateDes(profession.getDescription()));
		profesionEntity.setEstudios(validateEstudios(profession.getStudies()));
		return profesionEntity;
	}

	private String validateDes(String description) {
		return description != null ? description : "";
	}

	private List<EstudiosEntity> validateEstudios(List<Study> studies) {
		return studies != null && !studies.isEmpty()
				? studies.stream()
						.map(study -> estudiosMapperMaria.fromDomainToAdapter(study))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	public Profession fromAdapterToDomain(ProfesionEntity profesionEntity) {
		Profession profession = new Profession();
		profession.setIdentification(profesionEntity.getId());
		profession.setName(profesionEntity.getNom());
		profession.setDescription(validateDescription(profesionEntity.getDes()));
		profession.setStudies(validateStudies(profesionEntity.getEstudios()));
		return profession;
	}

	private String validateDescription(String des) {
		return des != null ? des : "";
	}

	private List<Study> validateStudies(List<EstudiosEntity> estudiosEntity) {
		return estudiosEntity != null && !estudiosEntity.isEmpty()
				? estudiosEntity.stream()
						.map(estudio -> {
							Study study = new Study();
							study.setGraduationDate(validateGraduationDate(estudio.getFecha()));
							study.setUniversityName(validateUniversityName(estudio.getUniver()));
							// Solo establece el ID de la persona para evitar recursión infinita
							Person minimalPerson = new Person();
							minimalPerson.setIdentification(estudio.getPersona().getCc());
							study.setPerson(minimalPerson);
							// Solo establece el ID de la profesión para evitar recursión infinita
							Profession minimalProfession = new Profession();
							minimalProfession.setIdentification(estudio.getProfesion().getId());
							study.setProfession(minimalProfession);
							return study;
						})
						.collect(Collectors.toList())
				: new ArrayList<>();
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
