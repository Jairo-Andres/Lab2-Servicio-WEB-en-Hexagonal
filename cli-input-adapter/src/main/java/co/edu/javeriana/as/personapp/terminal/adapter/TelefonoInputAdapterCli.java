package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private PersonInputPort personInputPort; // Para obtener el objeto Person por ID

    @Autowired
    private TelefonoMapperCli telefonoMapperCli;

    private PhoneInputPort phoneInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listarTelefonos() {
        log.info("Listing all phones in CLI Adapter");
        List<PhoneModelCli> phones = phoneInputPort.findAll().stream()
                .map(telefonoMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        phones.forEach(System.out::println);
    }

    public void crearTelefono(String number, String company, Integer ownerId) {
        log.info("Creating phone in CLI Adapter");

        try {
            // Obtener el objeto Person usando findOne
            Person owner = personInputPort.findOne(ownerId);
            Phone phone = new Phone(number, company, owner);
            phoneInputPort.create(phone);
            System.out.println("Phone created: " + phone);
        } catch (NoExistException e) {
            System.out.println("Owner with ID " + ownerId + " not found.");
        }
    }

    public void actualizarTelefono(String number, String company, Integer ownerId) {
        log.info("Updating phone in CLI Adapter");

        try {
            // Obtener el objeto Person usando findOne
            Person owner = personInputPort.findOne(ownerId);
            Phone phone = new Phone(number, company, owner);
            Phone updatedPhone = phoneInputPort.edit(number, phone);
            System.out.println("Phone updated: " + updatedPhone);
        } catch (NoExistException e) {
            System.out.println("Owner with ID " + ownerId + " not found.");
        }
    }

    public void borrarTelefono(String number) {
        log.info("Deleting phone in CLI Adapter");
        boolean deleted = phoneInputPort.delete(number);
        if (deleted) {
            System.out.println("Phone deleted successfully.");
        } else {
            System.out.println("Phone not found.");
        }
    }
}
