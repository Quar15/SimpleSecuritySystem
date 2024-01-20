package xyz.kacperjanas.securityapi.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.kacperjanas.securityapi.common.ESystemStatus;
import xyz.kacperjanas.securityapi.model.SecurityEvent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SecuritySystemCommand {
    private UUID id;
    private String prettyName;
    private String macAddress;
    private ESystemStatus status;
    private Date createdAt;
    private Date updatedAt;
    private Boolean favourite;

    private Set<SecurityEvent> events = new HashSet<>();
}
