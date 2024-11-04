package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.info("Creating profession in use case layer.");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer id, Profession profession) throws NoExistException {
        Profession existingProfession = professionPersistence.findById(id);
        if (existingProfession != null) {
            return professionPersistence.save(profession);
        } else {
            throw new NoExistException("Profession with ID " + id + " does not exist.");
        }
    }

    @Override
    public Boolean delete(Integer id) throws NoExistException {
        Profession existingProfession = professionPersistence.findById(id);
        if (existingProfession != null) {
            return professionPersistence.delete(id);
        } else {
            throw new NoExistException("Profession with ID " + id + " does not exist.");
        }
    }

    @Override
    public List<Profession> findAll() {
        return professionPersistence.findAll();
    }

    @Override
    public Profession findOne(Integer id) throws NoExistException {
        Profession profession = professionPersistence.findById(id);
        if (profession != null) {
            return profession;
        } else {
            throw new NoExistException("Profession with ID " + id + " does not exist.");
        }
    }

    @Override
    public Integer count() {
        return professionPersistence.findAll().size();
    }
}
