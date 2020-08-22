package com.jm.online_store.controller.simple;

import com.jm.online_store.model.User;
import com.jm.online_store.repository.ConfirmationTokenRepository;
import com.jm.online_store.service.interf.RoleService;
import com.jm.online_store.service.interf.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class RegistrationController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;


    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }


    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("userForm") @Validated User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("BindingResult in registerUserAccount hasErrors: {}", bindingResult);
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            log.debug("Passwords do not match : passwordConfirmError");
            model.addAttribute("passwordConfirmError", "Пароли не совпадают");
            return "registration";
        }
        if (userService.emailExist(userForm.getEmail())){
            log.debug("User with same email already exists : emailError ");
            model.addAttribute("emailError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        userService.regNewAccount(userForm);
        model.addAttribute("message", "Please check your email!");
        return "successfulRegister";
    }


    @GetMapping("/activate/{token}")
    public String activate(Model model, @PathVariable String token, HttpServletRequest request) {
        userService.activateUser(token, request);
        return "redirect:/customer";
    }
}
