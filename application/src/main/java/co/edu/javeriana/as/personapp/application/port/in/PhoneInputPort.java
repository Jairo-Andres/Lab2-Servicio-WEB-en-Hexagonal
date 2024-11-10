package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

import java.util.List;

@Port
public interface PhoneInputPort {
    public void setPersistence(PhoneOutputPort phoneOutputPort);

    public List<Phone> findAll();

    public Phone create(Phone phone);

    public Phone edit(String number, Phone phone) throws NoExistException;

    public Boolean delete(String number);
}
