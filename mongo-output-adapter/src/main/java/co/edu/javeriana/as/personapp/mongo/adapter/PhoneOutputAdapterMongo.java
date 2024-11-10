package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMongo telefonoRepositoryMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    @Override
    public Phone save(Phone phone) {
        log.info("Saving phone to MongoDB");
        TelefonoDocument document = telefonoMapperMongo.fromDomainToAdapter(phone);
        TelefonoDocument savedDocument = telefonoRepositoryMongo.save(document);
        return telefonoMapperMongo.fromAdapterToDomain(savedDocument);
    }

    @Override
    public Boolean delete(String number) {
        log.info("Deleting phone from MongoDB with number: {}", number);
        telefonoRepositoryMongo.deleteById(number);
        return telefonoRepositoryMongo.findById(number).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.info("Fetching all phones from MongoDB");
        return telefonoRepositoryMongo.findAll()
                .stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        log.info("Finding phone by number in MongoDB: {}", number);
        return telefonoRepositoryMongo.findById(number)
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
