package xyz.kacperjanas.securityapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.kacperjanas.securityapi.commands.AuthorizationRequestCommand;
import xyz.kacperjanas.securityapi.converters.SecuritySystemToSecuritySystemCommand;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

import java.util.Optional;
import java.util.UUID;

@Controller
public class SecuritySystemController {

    private SecuritySystemRepository securitySystemRepository;
    private SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand;

    public SecuritySystemController(SecuritySystemRepository securitySystemRepository, SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand) {
        this.securitySystemRepository = securitySystemRepository;
        this.securitySystemToSecuritySystemCommand = securitySystemToSecuritySystemCommand;
    }

    //region API

    @PostMapping(
            value = {"/api/system/authorize"},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Void> checkAuthorizationForSystem(@RequestBody AuthorizationRequestCommand command) {

        if (command.getSystemId() == null || command.getCardValue() == null) {
            System.out.println(
                    "@WARNING: BAD REQUEST for system authorization (" +
                    command.getSystemId() + ", " +
                    command.getCardValue() + ")"
            );
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(command.getSystemId());
        if (securitySystemOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SecuritySystem securitySystem = securitySystemOptional.get();
        if (! securitySystem.hasCardWithValue(command.getCardValue())) {
            System.out.println(
                    "@WARNING: UNAUTHORIZED REQUEST for system authorization (" +
                            command.getSystemId() + ", " +
                            command.getCardValue() + ")"
            );
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //endregion

    @GetMapping(value = {"/system/{id}/show"})
    public String getSystemDetails(Model model, @PathVariable("id") UUID id) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(id);

        if (securitySystemOptional.isEmpty()) {
            return "error/404";
        }

        model.addAttribute("system", securitySystemToSecuritySystemCommand.convert(
                securitySystemOptional.get()
        ));
        return "system/show";
    }
}
