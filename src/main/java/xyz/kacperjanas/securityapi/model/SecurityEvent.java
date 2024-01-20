package xyz.kacperjanas.securityapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;

import java.util.Date;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne()
    private SecuritySystem system;

    public SecurityEvent(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SecurityEvent() {}
}
