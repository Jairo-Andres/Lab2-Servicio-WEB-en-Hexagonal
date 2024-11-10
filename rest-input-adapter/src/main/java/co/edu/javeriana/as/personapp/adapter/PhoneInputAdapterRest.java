package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneInputAdapterRest {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperRest telefonoMapperRest;

    private PhoneInputPort phoneInputPort;

    private void setPhoneOutputPortInjection(String dbOption) {
        phoneInputPort = dbOption.equalsIgnoreCase("MONGO") ? new PhoneUseCase(phoneOutputPortMongo)
                : new PhoneUseCase(phoneOutputPortMaria);
    }

    public List<PhoneResponse> getAllPhones(String dbOption) {
        setPhoneOutputPortInjection(dbOption);
        return phoneInputPort.findAll().stream()
                .map(phone -> telefonoMapperRest.fromDomainToAdapterRest(phone, dbOption))
                .collect(Collectors.toList());
    }

    public PhoneResponse createPhone(PhoneRequest request) {
        setPhoneOutputPortInjection(request.getDatabase());
        Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request, request.getDatabase()));
        return telefonoMapperRest.fromDomainToAdapterRest(phone, request.getDatabase());
    }

    public PhoneResponse updatePhone(String number, PhoneRequest request) throws NoExistException {
        setPhoneOutputPortInjection(request.getDatabase());
        Phone phone = telefonoMapperRest.fromAdapterToDomain(request, request.getDatabase());
        Phone updatedPhone = phoneInputPort.edit(number, phone);
        return telefonoMapperRest.fromDomainToAdapterRest(updatedPhone, request.getDatabase());
    }

    public boolean deletePhone(String number, String dbOption) {
        setPhoneOutputPortInjection(dbOption);
        return phoneInputPort.delete(number);
    }
}
