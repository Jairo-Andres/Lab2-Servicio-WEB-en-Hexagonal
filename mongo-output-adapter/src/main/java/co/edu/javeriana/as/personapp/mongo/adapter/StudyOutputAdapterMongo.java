package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMongo estudiosRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Saving study in MongoDB");
        EstudiosDocument estudiosDocument = estudiosMapperMongo.fromDomainToAdapter(study);
        EstudiosDocument savedDocument = estudiosRepositoryMongo.save(estudiosDocument);
        return estudiosMapperMongo.fromAdapterToDomain(savedDocument);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        log.debug("Deleting study in MongoDB with personId: {} and professionId: {}", personId, professionId);
        String id = personId + "-" + professionId;
        estudiosRepositoryMongo.deleteById(id);
        return estudiosRepositoryMongo.findById(id).isEmpty();
    }

    @Override
    public List<Study> findAll() {
        log.debug("Fetching all studies from MongoDB");
        return estudiosRepositoryMongo.findAll().stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer personId, Integer professionId) {
        log.debug("Finding study by id in MongoDB");
        String id = personId + "-" + professionId;
        return estudiosRepositoryMongo.findById(id)
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
