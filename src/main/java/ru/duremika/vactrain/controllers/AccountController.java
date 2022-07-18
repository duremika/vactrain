package ru.duremika.vactrain.controllers;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.duremika.vactrain.DTO.UserDTO;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;
import ru.duremika.vactrain.services.UserService;
import ru.duremika.vactrain.services.VerificationTokenService;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class AccountController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    public AccountController(UserService userService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO, Model model) {
        model.addAttribute("userDTO", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public String save(
            @Valid UserDTO userDTO,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (userService.isExists(userDTO.getUsername())) {
            FieldError error = new FieldError("userDTO", "username", "User name already exists");
            bindingResult.addError(error);
        }

        if (userDTO.getPassword() != null && userDTO.getRpassword() != null) {
            if (!userDTO.getPassword().equals(userDTO.getRpassword())) {
                FieldError error = new FieldError("userDTO", "rpassword", "Passwords do not match");
                bindingResult.addError(error);
            }
        }

        if (bindingResult.hasErrors()) {
            return "/register";
        }
        userService.registry(userDTO);
        attributes.addAttribute("message",
                "A verification email has been sent to " + userDTO.getEmail());
        return "redirect:/login";
    }

    @GetMapping("/activation")
    public String activation(@RequestParam("token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if (verificationToken == null) {
            model.addAttribute("message", "Verification token is invalid");

        } else {
            User user = verificationToken.getUser();

            if (!user.isEnabled()) {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (verificationToken.getExpiryDate().before(now)) {
                    model.addAttribute("message", "Verification token has expired");
                } else {
                    user.setEnabled(true);
                    user.setCreatedAt(now);
                    userService.save(user);
                    verificationTokenService.remove(token);
                    model.addAttribute("message", "Account is successfully activated");

                }
            } else {
                model.addAttribute("message", "Account is already activated");
            }


        }
        return "/activation";
    }

}


