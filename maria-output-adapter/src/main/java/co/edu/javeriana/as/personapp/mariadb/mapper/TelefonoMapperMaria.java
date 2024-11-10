package co.edu.javeriana.as.personapp.mariadb.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	public TelefonoEntity fromDomainToAdapter(Phone phone) {
		TelefonoEntity telefonoEntity = new TelefonoEntity();
		telefonoEntity.setNum(phone.getNumber());
		telefonoEntity.setOper(phone.getCompany());
		// En lugar de hacer una conversión completa, solo almacena la identificación
		// del propietario
		PersonaEntity duenio = new PersonaEntity();
		duenio.setCc(phone.getOwner().getIdentification());
		telefonoEntity.setDuenio(duenio);
		return telefonoEntity;
	}

	public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
		Phone phone = new Phone();
		phone.setNumber(telefonoEntity.getNum());
		phone.setCompany(telefonoEntity.getOper());
		// Solo referencia el ID del propietario sin convertirlo completamente
		Person owner = new Person();
		owner.setIdentification(telefonoEntity.getDuenio().getCc());
		phone.setOwner(owner);
		return phone;
	}

	private @NonNull Person validateOwner(PersonaEntity duenio) {
		return duenio != null ? personaMapperMaria.fromAdapterToDomain(duenio) : new Person();
	}
}