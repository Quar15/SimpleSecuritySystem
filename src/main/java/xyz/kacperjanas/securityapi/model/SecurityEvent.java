package xyz.kacperjanas.securityapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import xyz.kacperjanas.securityapi.common.EEventType;
import xyz.kacperjanas.securityapi.repositories.SecurityEventRepository;

import java.util.Date;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private EEventType eventType;

    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne()
    private SecuritySystem system;

    public SecurityEvent(EEventType eventType, Date createdAt) {
        this.eventType = eventType;
        this.createdAt = createdAt;
    }

    public SecurityEvent() {}

    public static void CreateAndSave(EEventType eventType, Date createdAt, SecuritySystem securitySystem, SecurityEventRepository repository) {
        SecurityEvent securityEvent = new SecurityEvent(eventType, createdAt);
        securityEvent.setSystem(securitySystem);
        repository.save(securityEvent);
    }
}
