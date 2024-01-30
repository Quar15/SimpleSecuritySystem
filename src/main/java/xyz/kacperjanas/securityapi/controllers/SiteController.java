package xyz.kacperjanas.securityapi.controllers;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.kacperjanas.securityapi.repositories.SecurityEventRepository;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

@Controller
public class SiteController {

    private SecurityEventRepository securityEventRepository;
    private SecuritySystemRepository securitySystemRepository;

    public SiteController(SecurityEventRepository securityEventRepository, SecuritySystemRepository securitySystemRepository) {
        this.securityEventRepository = securityEventRepository;
        this.securitySystemRepository = securitySystemRepository;
    }

    @GetMapping(value = {"/"})
    public String index(Model model) {
        model.addAttribute(
                "favSystems",
                securitySystemRepository.findAllByFavourite(true)

        );

        model.addAttribute(
                "events",
                securityEventRepository.findFirst10By(Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        return "index";
    }
}
