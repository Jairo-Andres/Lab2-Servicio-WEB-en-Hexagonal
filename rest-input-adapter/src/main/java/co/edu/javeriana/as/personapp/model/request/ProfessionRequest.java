package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionRequest {
    private Integer id;
    private String name;
    private String description;
    private String database; // Asegúrate de tener este campo

    public String getDatabase() {
        return database;
    }
}
