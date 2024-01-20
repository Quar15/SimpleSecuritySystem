package xyz.kacperjanas.securityapi.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizationRequestCommand {
    private UUID systemId;
    private String cardValue;
}
