package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private EstudioMapperCli estudioMapperCli;

    private StudyInputPort studyInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listarEstudios() {
        log.info("Listing all studies in CLI Adapter");
        List<EstudioModelCli> estudios = studyInputPort.findAll().stream()
                .map(estudioMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        estudios.forEach(System.out::println);
    }

    public void crearEstudio(Integer personId, Integer professionId, String universityName, LocalDate graduationDate) {
        log.info("Creating study in CLI Adapter");

        Person person = new Person();
        person.setIdentification(personId);

        Profession profession = new Profession();
        profession.setIdentification(professionId);

        Study study = new Study(person, profession, graduationDate, universityName);
        studyInputPort.create(study);
        System.out.println("Study created: " + study);
    }

    public void actualizarEstudio(Integer personId, Integer professionId, String universityName,
            LocalDate graduationDate) {
        log.info("Updating study in CLI Adapter");

        try {
            Person person = new Person();
            person.setIdentification(personId);

            Profession profession = new Profession();
            profession.setIdentification(professionId);

            Study study = new Study(person, profession, graduationDate, universityName);
            Study updatedStudy = studyInputPort.edit(personId, professionId, study);
            System.out.println("Study updated: " + updatedStudy);
        } catch (NoExistException e) {
            System.out.println("Error updating study: " + e.getMessage());
        }
    }

    public void borrarEstudio(Integer personId, Integer professionId) {
        log.info("Deleting study in CLI Adapter");

        try {
            boolean deleted = studyInputPort.delete(personId, professionId);
            if (deleted) {
                System.out.println("Study deleted successfully.");
            } else {
                System.out.println("Study not found.");
            }
        } catch (NoExistException e) {
            System.out.println("Error deleting study: " + e.getMessage());
        }
    }
}
