package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMongo profesionRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    @Override
    public Profession save(Profession profession) {
        log.debug("Saving profession in MongoDB");
        ProfesionDocument persistedProfession = profesionRepositoryMongo
                .save(profesionMapperMongo.fromDomainToAdapter(profession));
        return profesionMapperMongo.fromAdapterToDomain(persistedProfession);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Deleting profession in MongoDB");
        profesionRepositoryMongo.deleteById(identification);
        return profesionRepositoryMongo.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> findAll() {
        log.debug("Finding all professions in MongoDB");
        return profesionRepositoryMongo.findAll().stream()
                .map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Finding profession by id in MongoDB");
        return profesionRepositoryMongo.findById(identification)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
