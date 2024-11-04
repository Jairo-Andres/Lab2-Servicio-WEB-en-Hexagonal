package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfessionMapperRest;
import co.edu.javeriana.as.personapp.mariadb.adapter.ProfessionOutputAdapterMaria;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import co.edu.javeriana.as.personapp.mongo.adapter.ProfessionOutputAdapterMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputAdapterMaria professionOutputAdapterMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputAdapterMongo professionOutputAdapterMongo;

    @Autowired
    private ProfessionMapperRest professionMapperRest;

    private ProfessionInputPort professionInputPort;

    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputAdapterMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputAdapterMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfessionResponse> getAll(String database) {
        try {
            setProfessionOutputPortInjection(database);
            return professionInputPort.findAll().stream()
                    .map(profession -> professionMapperRest.fromDomainToAdapterRest(profession, database))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public ProfessionResponse create(ProfessionRequest request) {
        try {
            String database = setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.create(professionMapperRest.fromAdapterToDomain(request));
            return professionMapperRest.fromDomainToAdapterRest(profession, database);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfessionResponse update(Integer id, ProfessionRequest request) {
        try {
            setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionMapperRest.fromAdapterToDomain(request);
            Profession updatedProfession = professionInputPort.edit(id, profession);
            return professionMapperRest.fromDomainToAdapterRest(updatedProfession, request.getDatabase());
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id, String database) {
        try {
            setProfessionOutputPortInjection(database);
            return professionInputPort.delete(id);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
