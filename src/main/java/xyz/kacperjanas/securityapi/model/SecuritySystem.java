package xyz.kacperjanas.securityapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class SecuritySystem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String prettyName;
    private String macAddress;
    private SystemStatus status;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    private Boolean favourite;

    // @ManyToMany(mappedBy = "artists", cascade = CascadeType.REMOVE)
    // private Set<Song> songs = new HashSet<>();


    public SecuritySystem(String prettyName, String macAddress, SystemStatus status, Boolean favourite) {
        this.prettyName = prettyName;
        this.macAddress = macAddress;
        this.status = status;
        this.favourite = favourite;
    }

    public SecuritySystem() {}
}
