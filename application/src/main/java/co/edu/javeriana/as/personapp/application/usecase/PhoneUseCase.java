package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;

    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
        log.info("Persistence set to: {}", phonePersistence.getClass().getSimpleName());
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Creating phone in use case layer.");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(String number, Phone phone) {
        try {
            Phone existingPhone = phonePersistence.findById(number);
            if (existingPhone != null) {
                return phonePersistence.save(phone);
            } else {
                throw new NoExistException("Phone with number " + number + " does not exist, cannot be edited.");
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean delete(String number) {
        try {
            Phone existingPhone = phonePersistence.findById(number);
            if (existingPhone != null) {
                return phonePersistence.delete(number);
            } else {
                throw new NoExistException("Phone with number " + number + " does not exist, cannot be deleted.");
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Phone> findAll() {
        log.info("Output adapter: " + phonePersistence.getClass());
        return phonePersistence.find();
    }
}
