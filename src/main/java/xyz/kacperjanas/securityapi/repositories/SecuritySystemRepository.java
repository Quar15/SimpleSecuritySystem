package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

import java.util.UUID;

public interface SecuritySystemRepository extends CrudRepository<SecuritySystem, UUID> {
}
