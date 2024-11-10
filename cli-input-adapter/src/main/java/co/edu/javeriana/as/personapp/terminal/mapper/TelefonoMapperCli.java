package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;

@Mapper
public class TelefonoMapperCli {

    public PhoneModelCli fromDomainToAdapterCli(Phone phone) {
        return new PhoneModelCli(phone.getNumber(), phone.getCompany(), phone.getOwner().getIdentification());
    }
}
