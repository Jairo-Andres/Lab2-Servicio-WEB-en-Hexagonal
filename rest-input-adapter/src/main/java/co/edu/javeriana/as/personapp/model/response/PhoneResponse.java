package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneResponse {
    private String number;
    private String company;
    private Integer duenio; // ID del dueño
    private String database;
    private String status;
}
