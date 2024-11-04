package co.edu.javeriana.as.personapp.mariadb.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMaria profesionRepositoryMaria;

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Override
    public Profession save(Profession profession) {
        log.debug("Saving profession in MariaDB");
        ProfesionEntity persistedProfession = profesionRepositoryMaria
                .save(profesionMapperMaria.fromDomainToAdapter(profession));
        return profesionMapperMaria.fromAdapterToDomain(persistedProfession);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Deleting profession in MariaDB");
        if (profesionRepositoryMaria.existsById(identification)) {
            profesionRepositoryMaria.deleteById(identification);
            return true;
        } else {
            return false; // Retorna false si no encontró el ID
        }
    }

    @Override
    public List<Profession> findAll() {
        log.debug("Finding all professions in MariaDB");
        return profesionRepositoryMaria.findAll().stream()
                .map(profesionMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Finding profession by id in MariaDB");
        return profesionRepositoryMaria.findById(identification)
                .map(profesionMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
