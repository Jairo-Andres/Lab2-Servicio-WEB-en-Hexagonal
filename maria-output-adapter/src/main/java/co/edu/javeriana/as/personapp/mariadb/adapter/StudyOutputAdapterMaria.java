package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMaria estudiosRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        EstudiosEntity entity = estudiosMapperMaria.fromDomainToAdapter(study);
        EstudiosEntity savedEntity = estudiosRepositoryMaria.save(entity);
        return estudiosMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personId);
        estudiosRepositoryMaria.deleteById(pk);
        return estudiosRepositoryMaria.findById(pk).isEmpty();
    }

    @Override
    public List<Study> findAll() {
        return estudiosRepositoryMaria.findAll()
                .stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer personId, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personId);
        return estudiosRepositoryMaria.findById(pk)
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
