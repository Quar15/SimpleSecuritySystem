package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

public interface SecuritySystemRepository extends CrudRepository<SecuritySystem, Long> {
}
