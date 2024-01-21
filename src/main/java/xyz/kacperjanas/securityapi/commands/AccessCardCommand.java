package xyz.kacperjanas.securityapi.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AccessCardCommand {

    private Long id;
    private String cardValue;
    private Date createdAt;
    private Date updatedAt;
}
