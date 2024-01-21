package xyz.kacperjanas.securityapi.controllers;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.kacperjanas.securityapi.commands.EventRequestCommand;
import xyz.kacperjanas.securityapi.common.EEventType;
import xyz.kacperjanas.securityapi.model.SecurityEvent;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.repositories.SecurityEventRepository;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

import java.util.Date;
import java.util.Optional;

@Controller
public class SecurityEventController {

    private SecurityEventRepository securityEventRepository;
    private SecuritySystemRepository securitySystemRepository;

    public SecurityEventController(SecurityEventRepository securityEventRepository, SecuritySystemRepository securitySystemRepository) {
        this.securityEventRepository = securityEventRepository;
        this.securitySystemRepository = securitySystemRepository;
    }

//region API

    @PostMapping(
            value = {"/api/event"},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Void> checkAuthorizationForSystem(@RequestBody EventRequestCommand command) {

        if (command.getSystemId() == null || command.getTimestamp() == null) {
            System.out.println(
                    "@WARNING: BAD REQUEST for creating event (" +
                            command.getSystemId() + ", " +
                            command.getTimestamp() + ")"
            );
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(command.getSystemId());
        if (securitySystemOptional.isEmpty()) {
            System.out.println(
                    "@WARNING: System NOT FOUND while handling creating event (" +
                            command.getSystemId() + ", " +
                            command.getTimestamp() + ")"
            );
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        SecurityEvent securityEvent = new SecurityEvent(
                EEventType.MOVE_DETECTION,
                new Date(command.getTimestamp() * 1000) // Timestamp in Java for Date is in milliseconds
        );
        securityEvent.setSystem(securitySystemOptional.get());
        securityEventRepository.save(securityEvent);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //endregion

    @GetMapping(value = {"/event/list"})
    public String getSystemList(Model model) {
        model.addAttribute(
                "events",
                securityEventRepository.findAllBy(Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return "event/list";
    }
}
