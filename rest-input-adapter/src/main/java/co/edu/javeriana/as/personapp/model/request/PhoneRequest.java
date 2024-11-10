package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
    private String number;
    private String company;
    private Integer duenio; // ID del dueño en la tabla persona
    private String database; // Agrega este campo

    public String getDatabase() {
        return database;
    }
}
