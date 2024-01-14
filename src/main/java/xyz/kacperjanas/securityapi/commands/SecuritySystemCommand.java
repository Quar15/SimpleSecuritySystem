package xyz.kacperjanas.securityapi.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.kacperjanas.securityapi.model.SystemStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SecuritySystemCommand {
    private Long id;
    private String prettyName;
    private String macAddress;
    private SystemStatus status;
    private Date createdAt;
    private Date updatedAt;
    private Boolean favourite;
}
