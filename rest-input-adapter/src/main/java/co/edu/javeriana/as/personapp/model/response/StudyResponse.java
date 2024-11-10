package co.edu.javeriana.as.personapp.model.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyResponse {
    private Integer personId;
    private Integer professionId;
    private String universityName;
    private LocalDate graduationDate;
    private String database;
    private String status;
}
