package co.edu.javeriana.as.personapp.model.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyRequest {
    private Integer personId;
    private Integer professionId;
    private String universityName;
    private LocalDate graduationDate;
    private String database;
}
