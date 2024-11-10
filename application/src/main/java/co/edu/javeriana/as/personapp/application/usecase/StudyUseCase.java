package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(Integer personId, Integer professionId, Study study) throws NoExistException {
        Study existingStudy = studyPersistence.findById(personId, professionId);
        if (existingStudy != null) {
            return studyPersistence.save(study);
        } else {
            throw new NoExistException(
                    "Study with person ID " + personId + " and profession ID " + professionId + " does not exist.");
        }
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) throws NoExistException {
        Study existingStudy = studyPersistence.findById(personId, professionId);
        if (existingStudy != null) {
            return studyPersistence.delete(personId, professionId);
        } else {
            throw new NoExistException(
                    "Study with person ID " + personId + " and profession ID " + professionId + " does not exist.");
        }
    }

    @Override
    public List<Study> findAll() {
        return studyPersistence.findAll();
    }

    @Override
    public Study findById(Integer personId, Integer professionId) throws NoExistException {
        Study study = studyPersistence.findById(personId, professionId);
        if (study != null) {
            return study;
        } else {
            throw new NoExistException(
                    "Study with person ID " + personId + " and profession ID " + professionId + " does not exist.");
        }
    }
}
