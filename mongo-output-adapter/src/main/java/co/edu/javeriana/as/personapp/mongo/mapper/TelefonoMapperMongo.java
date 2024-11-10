package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		// En lugar de hacer una conversión completa, solo almacena la ID del
		// propietario
		telefonoDocument.setPrimaryDuenio(
				new PersonaDocument(phone.getOwner().getIdentification(), null, null, null, null, null, null));
		return telefonoDocument;
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		Phone phone = new Phone();
		phone.setNumber(telefonoDocument.getId());
		phone.setCompany(telefonoDocument.getOper());
		// Solo referencia el ID del propietario sin convertirlo completamente
		Person owner = new Person();
		owner.setIdentification(telefonoDocument.getPrimaryDuenio().getId());
		phone.setOwner(owner);
		return phone;
	}

	private @NonNull Person validateOwner(PersonaDocument duenio) {
		return duenio != null ? personaMapperMongo.fromAdapterToDomain(duenio) : new Person();
	}
}