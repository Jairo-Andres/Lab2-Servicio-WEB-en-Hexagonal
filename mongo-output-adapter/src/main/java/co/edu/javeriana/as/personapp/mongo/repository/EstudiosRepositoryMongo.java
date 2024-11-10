package co.edu.javeriana.as.personapp.mongo.repository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {
}
