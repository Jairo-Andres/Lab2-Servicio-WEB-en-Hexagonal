package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

@Mapper
public class StudyMapperRest {

    public Study fromAdapterToDomain(StudyRequest request, Person person, Profession profession) {
        Study study = new Study();
        study.setPerson(person);
        study.setProfession(profession);
        study.setUniversityName(request.getUniversityName());
        study.setGraduationDate(request.getGraduationDate());
        return study;
    }

    public StudyResponse fromDomainToAdapterRest(Study study, String database) {
        return new StudyResponse(
                study.getPerson().getIdentification(),
                study.getProfession().getIdentification(),
                study.getUniversityName(),
                study.getGraduationDate(),
                database,
                "OK");
    }
}
