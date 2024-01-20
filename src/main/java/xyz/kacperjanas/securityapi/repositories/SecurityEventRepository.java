package xyz.kacperjanas.securityapi.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.kacperjanas.securityapi.model.SecurityEvent;

public interface SecurityEventRepository extends CrudRepository<SecurityEvent, Long> {
}