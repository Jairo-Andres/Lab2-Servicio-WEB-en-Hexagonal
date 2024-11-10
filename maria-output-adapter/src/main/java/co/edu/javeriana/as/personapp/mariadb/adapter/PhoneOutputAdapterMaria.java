package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.info("Saving phone to MariaDB");
        TelefonoEntity entity = telefonoMapperMaria.fromDomainToAdapter(phone);
        TelefonoEntity savedEntity = telefonoRepositoryMaria.save(entity);
        return telefonoMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(String number) {
        log.info("Deleting phone from MariaDB with number: {}", number);
        telefonoRepositoryMaria.deleteById(number);
        return telefonoRepositoryMaria.findById(number).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.info("Fetching all phones from MariaDB");
        return telefonoRepositoryMaria.findAll()
                .stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        log.info("Finding phone by number in MariaDB: {}", number);
        return telefonoRepositoryMaria.findById(number)
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
