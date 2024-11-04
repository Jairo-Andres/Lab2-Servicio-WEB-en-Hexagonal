package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Mapper
public class ProfessionMapperCli {

    public ProfessionModelCli fromDomainToAdapterCli(Profession profession) {
        return new ProfessionModelCli(profession.getIdentification(), profession.getName(),
                profession.getDescription());
    }
}
