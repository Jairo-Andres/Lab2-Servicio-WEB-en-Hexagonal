package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfessionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperCli professionMapperCli;

    private ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listarProfesiones() {
        log.info("Listing all professions in CLI Adapter");
        List<ProfessionModelCli> professions = professionInputPort.findAll().stream()
                .map(professionMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        professions.forEach(System.out::println);
    }

    public void crearProfesion(Integer id, String nombre, String descripcion) {
        log.info("Creating profession in CLI Adapter");
        Profession profession = new Profession(id, nombre, descripcion);
        professionInputPort.create(profession);
        System.out.println("Profession created: " + profession);
    }

    public void actualizarProfesion(Integer id, String nombre, String descripcion) {
        log.info("Updating profession in CLI Adapter");
        try {
            Profession profession = new Profession(id, nombre, descripcion);
            Profession updatedProfession = professionInputPort.edit(id, profession);
            System.out.println("Profession updated: " + updatedProfession);
        } catch (NoExistException e) {
            System.out.println("Error updating profession: " + e.getMessage());
        }
    }

    public void borrarProfesion(Integer id) {
        log.info("Deleting profession in CLI Adapter");
        try {
            boolean deleted = professionInputPort.delete(id);
            if (deleted) {
                System.out.println("Profession deleted successfully.");
            } else {
                System.out.println("Profession not found.");
            }
        } catch (NoExistException e) {
            System.out.println("Error deleting profession: " + e.getMessage());
        }
    }
}
