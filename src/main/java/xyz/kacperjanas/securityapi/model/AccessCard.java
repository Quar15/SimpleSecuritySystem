package xyz.kacperjanas.securityapi.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class AccessCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cardValue;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

     @ManyToMany(cascade = CascadeType.REMOVE)
     private Set<SecuritySystem> systems = new HashSet<>();

    public AccessCard(String cardValue) {
        this.cardValue = cardValue;
    }

    public AccessCard() {}
}
