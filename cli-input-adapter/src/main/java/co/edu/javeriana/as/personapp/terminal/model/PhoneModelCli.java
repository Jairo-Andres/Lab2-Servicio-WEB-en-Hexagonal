package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneModelCli {
    private String number;
    private String company;
    private Integer ownerId; // Usando el ID del propietario en lugar del objeto Person completo
}
