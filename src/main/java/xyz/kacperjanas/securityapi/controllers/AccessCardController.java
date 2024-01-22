package xyz.kacperjanas.securityapi.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.kacperjanas.securityapi.commands.AccessCardCommand;
import xyz.kacperjanas.securityapi.converters.AccessCardCommandToAccessCard;
import xyz.kacperjanas.securityapi.converters.AccessCardToAccessCardCommand;
import xyz.kacperjanas.securityapi.model.AccessCard;
import xyz.kacperjanas.securityapi.repositories.AccessCardRepository;

import java.util.Optional;

@Controller
public class AccessCardController {
    AccessCardRepository accessCardRepository;

    AccessCardToAccessCardCommand accessCardToAccessCardCommand;
    AccessCardCommandToAccessCard accessCardCommandToAccessCard;

    public AccessCardController(AccessCardRepository accessCardRepository, AccessCardToAccessCardCommand accessCardToAccessCardCommand, AccessCardCommandToAccessCard accessCardCommandToAccessCard) {
        this.accessCardRepository = accessCardRepository;
        this.accessCardToAccessCardCommand = accessCardToAccessCardCommand;
        this.accessCardCommandToAccessCard = accessCardCommandToAccessCard;
    }

//region API



//endregion

    @GetMapping(value = {"/card/list"})
    public String getCardList(Model model) {
        model.addAttribute(
                "cards",
                accessCardRepository.findAllBy(Sort.by(Sort.Direction.DESC, "updatedAt"))
        );
        return "card/list";
    }

    @RequestMapping(value = {"/card/{id}/delete"})
    public String deleteCard(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            accessCardRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("flashMsg", "Deleted Card");
            redirectAttributes.addFlashAttribute("flashClass", "info");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("flashMsg", "Cannot delete Card with connected System");
            redirectAttributes.addFlashAttribute("flashClass", "error");
        }

        return "redirect:/card/list";
    }

    @GetMapping(value = {"/card/new"})
    public String newCard(Model model) {
        model.addAttribute("card", new AccessCardCommand());
        return "card/addedit";
    }

    @GetMapping(value = {"/card/{id}/edit"})
    public String editArtist(Model model, @PathVariable("id") Long id) {
        model.addAttribute("card", accessCardToAccessCardCommand.convert(
                accessCardRepository.findById(id).get()
        ));
        return "card/addedit";
    }

    @PostMapping("/card")
    public String saveOrUpdate(@ModelAttribute AccessCardCommand command, RedirectAttributes redirectAttributes) {

        if (command.getId() == null) {
            System.out.println("@INFO: Creating new access card");
            AccessCard detachedCard = accessCardCommandToAccessCard.convert(command);
            if (detachedCard == null) {
                return "redirect:/card/new";
            }
            accessCardRepository.save(detachedCard);
            redirectAttributes.addFlashAttribute("flashMsg", "Created Card");
            redirectAttributes.addFlashAttribute("flashClass", "info");
        } else {
            Optional<AccessCard> accessCardOptional = accessCardRepository.findById(command.getId());
            AccessCard accessCardFromDb = accessCardOptional.get();
            accessCardFromDb.setCardValue(command.getCardValue());
            accessCardRepository.save(accessCardFromDb);
            redirectAttributes.addFlashAttribute("flashMsg", "Updated Card");
            redirectAttributes.addFlashAttribute("flashClass", "info");
        }

        return "redirect:/card/list";
    }
}
