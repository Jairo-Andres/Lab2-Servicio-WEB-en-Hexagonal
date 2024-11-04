package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

@Mapper
public class ProfessionMapperRest {

    public ProfessionResponse fromDomainToAdapterRest(Profession profession, String database) {
        return new ProfessionResponse(
                profession.getIdentification(),
                profession.getName(),
                profession.getDescription(),
                database,
                "OK");
    }

    public Profession fromAdapterToDomain(ProfessionRequest request) {
        return new Profession(
                request.getId(),
                request.getName(),
                request.getDescription());
    }
}
