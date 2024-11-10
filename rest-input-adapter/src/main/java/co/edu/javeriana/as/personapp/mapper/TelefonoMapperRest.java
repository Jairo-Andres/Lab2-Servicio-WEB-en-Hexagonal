package co.edu.javeriana.as.personapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;

@Mapper
public class TelefonoMapperRest {
    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    public Phone fromAdapterToDomain(PhoneRequest request, String database) {
        Person owner = database.equalsIgnoreCase("MONGO")
                ? personOutputPortMongo.findById(request.getDuenio())
                : personOutputPortMaria.findById(request.getDuenio());

        return new Phone(
                request.getNumber(),
                request.getCompany(),
                owner);
    }

    public PhoneResponse fromDomainToAdapterRest(Phone phone, String database) {
        return new PhoneResponse(
                phone.getNumber(),
                phone.getCompany(),
                phone.getOwner().getIdentification(),
                database,
                "OK");
    }
}
