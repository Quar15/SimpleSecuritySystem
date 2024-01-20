package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.AccessCard;

public interface AccessCardRepository extends CrudRepository<AccessCard, Long> {
}
