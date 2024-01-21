package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

import java.util.UUID;

public interface SecuritySystemRepository extends CrudRepository<SecuritySystem, UUID> {
    Iterable<SecuritySystem> findAllBy(Sort sortBy);

    Iterable<SecuritySystem> findAllByFavourite(Boolean favourite);
    Long countByFavourite(Boolean favourite);
}
