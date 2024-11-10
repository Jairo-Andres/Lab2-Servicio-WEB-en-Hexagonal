package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

    public EstudioModelCli fromDomainToAdapterCli(Study study) {
        return new EstudioModelCli(
                study.getPerson().getIdentification(), // ID de la persona
                study.getProfession().getIdentification(), // ID de la profesión
                study.getUniversityName(),
                study.getGraduationDate().toString());
    }
}
