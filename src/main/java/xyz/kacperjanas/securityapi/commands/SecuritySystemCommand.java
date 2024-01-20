package xyz.kacperjanas.securityapi.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.kacperjanas.securityapi.common.ESystemStatus;

import java.util.Date;
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
}
