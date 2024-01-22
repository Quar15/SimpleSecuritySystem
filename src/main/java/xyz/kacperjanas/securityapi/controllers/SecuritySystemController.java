package xyz.kacperjanas.securityapi.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.kacperjanas.securityapi.commands.AuthorizationRequestCommand;
import xyz.kacperjanas.securityapi.commands.SecuritySystemCommand;
import xyz.kacperjanas.securityapi.common.EEventType;
import xyz.kacperjanas.securityapi.common.ESystemStatus;
import xyz.kacperjanas.securityapi.converters.AccessCardToAccessCardCommand;
import xyz.kacperjanas.securityapi.converters.SecuritySystemCommandToSecuritySystem;
import xyz.kacperjanas.securityapi.converters.SecuritySystemToSecuritySystemCommand;
import xyz.kacperjanas.securityapi.model.AccessCard;
import xyz.kacperjanas.securityapi.model.SecurityEvent;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.repositories.AccessCardRepository;
import xyz.kacperjanas.securityapi.repositories.SecurityEventRepository;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

import java.util.*;

@Controller
public class SecuritySystemController {

    private final int MAX_FAVOURITE_SYSTEMS = 5;

    private SecurityEventRepository securityEventRepository;
    private SecuritySystemRepository securitySystemRepository;
    private AccessCardRepository accessCardRepository;
    private SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand;
    private SecuritySystemCommandToSecuritySystem securitySystemCommandToSecuritySystem;
    private AccessCardToAccessCardCommand accessCardToAccessCardCommand;

    public SecuritySystemController(
            SecurityEventRepository securityEventRepository,
            SecuritySystemRepository securitySystemRepository,
            AccessCardRepository accessCardRepository,
            SecuritySystemToSecuritySystemCommand securitySystemToSecuritySystemCommand,
            SecuritySystemCommandToSecuritySystem securitySystemCommandToSecuritySystem
    ) {
        this.securityEventRepository = securityEventRepository;
        this.securitySystemRepository = securitySystemRepository;
        this.accessCardRepository = accessCardRepository;
        this.securitySystemToSecuritySystemCommand = securitySystemToSecuritySystemCommand;
        this.securitySystemCommandToSecuritySystem = securitySystemCommandToSecuritySystem;
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

        EEventType eventType = switch (securitySystem.getStatus()) {
            case LOCKED -> {
                securitySystem.setStatus(ESystemStatus.UNLOCKED);
                yield EEventType.UNLOCK;
            }
            case UNLOCKED -> {
                securitySystem.setStatus(ESystemStatus.LOCKED);
                yield EEventType.LOCK;
            }
            default -> EEventType.EVENT;
        };
        securitySystemRepository.save(securitySystem);

        SecurityEvent securityEvent = new SecurityEvent(eventType, new Date());
        securityEvent.setSystem(securitySystem);
        securityEventRepository.save(securityEvent);

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

    @GetMapping(value = {"/system/list"})
    public String getSystemList(Model model) {
        model.addAttribute(
                "systems",
                securitySystemRepository.findAllBy(Sort.by(Sort.Direction.DESC, "updatedAt"))
        );
        return "system/list";
    }

    @RequestMapping(value = {"/system/{id}/delete"})
    public String deleteSystem(Model model, @PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            securitySystemRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("flashMsg", "Deleted System");
            redirectAttributes.addFlashAttribute("flashClass", "info");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("flashMsg", "Cannot delete System with connected Access Cards or Events");
            redirectAttributes.addFlashAttribute("flashClass", "error");
        }


        return "redirect:/system/list";
    }

    @GetMapping(value = {"/system/new"})
    public String newSystem(Model model) {
        model.addAttribute("system", new SecuritySystemCommand());
        return "system/addedit";
    }

    @GetMapping(value = {"/system/{id}/edit"})
    public String editSystem(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("system", securitySystemToSecuritySystemCommand.convert(
                securitySystemRepository.findById(id).get()
        ));
        return "system/addedit";
    }

    @PostMapping("/system")
    public String saveOrUpdate(@ModelAttribute SecuritySystemCommand command) {

        if (command.getId() == null) {
            System.out.println("@INFO: Creating new security system");
            SecuritySystem detachedSystem = securitySystemCommandToSecuritySystem.convert(command);
            if (detachedSystem == null) {
                return "redirect:/system/new";
            }

            detachedSystem.setStatus(ESystemStatus.UNLOCKED);
            detachedSystem.setFavourite(false);

            SecuritySystem savedSecuritySystem = securitySystemRepository.save(detachedSystem);
            return "redirect:/system/" + savedSecuritySystem.getId() + "/show";
        } else {
            Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(command.getId());
            SecuritySystem securitySystemFromDb = securitySystemOptional.get();
            securitySystemFromDb.setPrettyName(command.getPrettyName());
            securitySystemFromDb.setMacAddress(command.getMacAddress());
            securitySystemRepository.save(securitySystemFromDb);

            return "redirect:/system/" + securitySystemFromDb.getId() + "/show";
        }
    }

    @GetMapping("/system/{id}/favourite")
    public String favourite(Model model, @PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(id);

        if (securitySystemOptional.isEmpty()) {
            return "error/404";
        }

        SecuritySystem securitySystemFromDb = securitySystemOptional.get();
        Long favouritesCount = securitySystemRepository.countByFavourite(true);
        if (favouritesCount > MAX_FAVOURITE_SYSTEMS) {
            redirectAttributes.addFlashAttribute("flashMsg", "Max number of favourites reached!");
            redirectAttributes.addFlashAttribute("flashClass", "warning");
        } else {
            securitySystemFromDb.setFavourite(!securitySystemFromDb.getFavourite());
            securitySystemRepository.save(securitySystemFromDb);

            Long newFavouritesCount = securitySystemRepository.countByFavourite(true);
            redirectAttributes.addFlashAttribute(
                    "flashMsg",
                    "Added System " +
                        securitySystemFromDb.getPrettyName() +
                        " to favourites " +
                        "[" + (newFavouritesCount) + "/" + MAX_FAVOURITE_SYSTEMS + "]"
            );
            redirectAttributes.addFlashAttribute("flashClass", "info");
        }


        return "redirect:/system/list";
    }

    @GetMapping("/system/{id}/add-card")
    public String addCard(Model model, @PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(id);

        if (securitySystemOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashMsg", "Could not find System");
            redirectAttributes.addFlashAttribute("flashClass", "error");
            return "redirect:/system/list";
        }

        SecuritySystem securitySystemFromDb = securitySystemOptional.get();
        model.addAttribute("system", securitySystemToSecuritySystemCommand.convert(
                securitySystemFromDb
        ));

        Iterable<AccessCard> allAccessCards = accessCardRepository.findAll();
        Set<AccessCard> securitySystemAccessCards = securitySystemFromDb.getAccessCards();

        Iterator<AccessCard> iterator = allAccessCards.iterator();
        Set<AccessCard> notConnectedAccessCards = new HashSet<>();
        while(iterator.hasNext()) {
            AccessCard currAccessCard = iterator.next();
            if (!securitySystemAccessCards.contains(currAccessCard))
                notConnectedAccessCards.add(currAccessCard);
        }

        model.addAttribute("cards", notConnectedAccessCards);
        model.addAttribute("connectedCards", securitySystemAccessCards);

        return "system/addcardtosystem";
    }

    @PostMapping("/system/{system_id}/add-card/{card_id}")
    public String addCardToSystem(
            Model model,
            @PathVariable("system_id") UUID systemId,
            @PathVariable("card_id") Long cardId,
            RedirectAttributes redirectAttributes
    ) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(systemId);

        if (securitySystemOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashMsg", "Could not find System");
            redirectAttributes.addFlashAttribute("flashClass", "error");
            return "redirect:/system/list";
        }

        Optional<AccessCard> accessCardOptional = accessCardRepository.findById(cardId);

        if (accessCardOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashMsg", "Could not find Card");
            redirectAttributes.addFlashAttribute("flashClass", "error");
            return "redirect:/system/list";
        }

        SecuritySystem securitySystemFromDb = securitySystemOptional.get();
        AccessCard accessCardFromDb = accessCardOptional.get();

        accessCardFromDb.getSystems().add(securitySystemFromDb);
        accessCardRepository.save(accessCardFromDb);

        redirectAttributes.addFlashAttribute("flashMsg", "Added Card to System");
        redirectAttributes.addFlashAttribute("flashClass", "info");

        return "redirect:/system/" + systemId + "/add-card";
    }

    @PostMapping("/system/{system_id}/remove-card/{card_id}")
    public String removeCardFromSystem(
            Model model,
            @PathVariable("system_id") UUID systemId,
            @PathVariable("card_id") Long cardId,
            RedirectAttributes redirectAttributes
    ) {
        Optional<SecuritySystem> securitySystemOptional = securitySystemRepository.findById(systemId);

        if (securitySystemOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashMsg", "Could not find System");
            redirectAttributes.addFlashAttribute("flashClass", "error");
            return "redirect:/system/list";
        }

        Optional<AccessCard> accessCardOptional = accessCardRepository.findById(cardId);

        if (accessCardOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashMsg", "Could not find Card");
            redirectAttributes.addFlashAttribute("flashClass", "error");
            return "redirect:/system/list";
        }

        SecuritySystem securitySystemFromDb = securitySystemOptional.get();
        AccessCard accessCardFromDb = accessCardOptional.get();

        accessCardFromDb.getSystems().remove(securitySystemFromDb);
        accessCardRepository.save(accessCardFromDb);

        redirectAttributes.addFlashAttribute("flashMsg", "Removed Card from System");
        redirectAttributes.addFlashAttribute("flashClass", "info");

        return "redirect:/system/" + systemId + "/add-card";
    }
}
