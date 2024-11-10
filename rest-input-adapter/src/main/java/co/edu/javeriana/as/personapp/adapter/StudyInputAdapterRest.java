package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StudyInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private StudyMapperRest studyMapperRest;

    private StudyInputPort studyInputPort;
    private PersonOutputPort personOutputPort;
    private ProfessionOutputPort professionOutputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            personOutputPort = personOutputPortMaria;
            professionOutputPort = professionOutputPortMaria;
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            personOutputPort = personOutputPortMongo;
            professionOutputPort = professionOutputPortMongo;
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<StudyResponse> getAll(String database) {
        try {
            setStudyOutputPortInjection(database);
            return studyInputPort.findAll().stream()
                    .map(study -> studyMapperRest.fromDomainToAdapterRest(study, database))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public StudyResponse create(StudyRequest request) {
        try {
            String database = setStudyOutputPortInjection(request.getDatabase());

            // Retrieve Person and Profession based on IDs in the request
            Person person = personOutputPort.findById(request.getPersonId());
            Profession profession = professionOutputPort.findById(request.getProfessionId());

            if (person == null || profession == null) {
                throw new IllegalArgumentException("Person or Profession not found for the given IDs");
            }

            // Map request to Study domain model with retrieved Person and Profession
            Study study = studyMapperRest.fromAdapterToDomain(request, person, profession);
            Study createdStudy = studyInputPort.create(study);
            return studyMapperRest.fromDomainToAdapterRest(createdStudy, database);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public StudyResponse update(Integer personId, Integer professionId, StudyRequest request) {
        try {
            setStudyOutputPortInjection(request.getDatabase());

            // Retrieve Person and Profession based on IDs in the request
            Person person = personOutputPort.findById(personId);
            Profession profession = professionOutputPort.findById(professionId);

            if (person == null || profession == null) {
                throw new IllegalArgumentException("Person or Profession not found for the given IDs");
            }

            // Map request to Study domain model with retrieved Person and Profession
            Study study = studyMapperRest.fromAdapterToDomain(request, person, profession);
            Study updatedStudy = studyInputPort.edit(personId, professionId, study);
            return studyMapperRest.fromDomainToAdapterRest(updatedStudy, request.getDatabase());
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer personId, Integer professionId, String database) {
        try {
            setStudyOutputPortInjection(database);
            return studyInputPort.delete(personId, professionId);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
