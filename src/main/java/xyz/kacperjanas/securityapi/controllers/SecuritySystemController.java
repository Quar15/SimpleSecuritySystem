package xyz.kacperjanas.securityapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.kacperjanas.securityapi.converters.SecuritySystemToSecuritySystemCommand;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

@Controller
public class SecuritySystemController {

    private SecuritySystemRepository securitySystemRepository;
    private SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand;

    public SecuritySystemController(SecuritySystemRepository securitySystemRepository, SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand) {
        this.securitySystemRepository = securitySystemRepository;
        this.securitySystemToSecuritySystemCommand = securitySystemToSecuritySystemCommand;
    }

    @GetMapping(value = {"/system/{id}/show"})
    public String getSystemDetails(Model model, @PathVariable("id") Long id) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(id);

        if (securitySystemOptional.isEmpty()) {
            return "error/404";
        }

        model.addAttribute("system", securitySystemToSecuritySystemCommand.convert(
                securitySystemRepository.findById(id).get()
                securitySystemOptional.get()
        ));
        return "system/show";
    }
}
