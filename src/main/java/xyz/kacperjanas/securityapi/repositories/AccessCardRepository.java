package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.AccessCard;

public interface AccessCardRepository extends CrudRepository<AccessCard, Long> {
    Iterable<AccessCard> findAllBy(Sort sortBy);
}
