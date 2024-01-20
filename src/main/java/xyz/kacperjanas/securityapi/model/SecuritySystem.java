package xyz.kacperjanas.securityapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import xyz.kacperjanas.securityapi.common.ESystemStatus;

import java.util.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class SecuritySystem {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    private String prettyName;
    private String macAddress;
    private ESystemStatus status;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    private Boolean favourite;

    @ManyToMany(mappedBy = "systems", cascade = CascadeType.REMOVE)
    private Set<AccessCard> accessCards = new HashSet<>();

    @OneToMany(mappedBy = "system")
    private Set<SecurityEvent> events = new HashSet<>();

    public SecuritySystem(String prettyName, String macAddress, ESystemStatus status, Boolean favourite) {
        this.prettyName = prettyName;
        this.macAddress = macAddress;
        this.status = status;
        this.favourite = favourite;
    }

    public SecuritySystem() {}

    public Boolean hasCardWithValue(String searchedCardValue) {
        Set<AccessCard> accessCards = this.getAccessCards();

        for(AccessCard card : accessCards) {
            if (searchedCardValue.equals(card.getCardValue())) {
                return true;
            }
        }

        return false;
    }
}
