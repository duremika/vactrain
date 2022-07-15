package ru.duremika.vactrain.controllers;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.duremika.vactrain.DTO.UserDTO;
import ru.duremika.vactrain.services.UserService;

import javax.validation.Valid;

@Controller
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
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
        if (userService.isExists(userDTO.getUsername())){
            FieldError error = new FieldError("userDTO", "username", "User name already exists");
            bindingResult.addError(error);
        }

        if (userDTO.getPassword() != null && userDTO.getRpassword() != null){
            if (!userDTO.getPassword().equals(userDTO.getRpassword())){
                FieldError error = new FieldError("userDTO", "rpassword", "Passwords do not match");
                bindingResult.addError(error);
            }
        }

        if (bindingResult.hasErrors()){
            return "/register";
        }
        userService.registry(userDTO);
        attributes.addAttribute("message", "You have successfully registered");
        return "redirect:/login";
    }


}


