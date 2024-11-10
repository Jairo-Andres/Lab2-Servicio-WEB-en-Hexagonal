package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;

import java.util.List;

@Port
public interface StudyInputPort {

    public Study create(Study study);

    public Study edit(Integer personId, Integer professionId, Study study) throws NoExistException;

    public Boolean delete(Integer personId, Integer professionId) throws NoExistException;

    public List<Study> findAll();

    public Study findById(Integer personId, Integer professionId) throws NoExistException;

    public void setPersistence(StudyOutputPort studyOutputPort);
}
