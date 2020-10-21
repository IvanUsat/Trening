package com.jm.online_store.controller.simple;

import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.CommentService;
import com.jm.online_store.service.interf.UserService;
import com.jm.online_store.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * CustomerController контроллер для пользователя с ролью "Customer"
 */
@AllArgsConstructor
@Controller
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    private final UserService userService;
    private final CommentService commentService;

    /**
     * метод получения данных зарегистрированного пользователя.
     * формирование модели для вывода в "view"
     * модель данных, построенных на основе зарегистрированного User
     *
     * @return
     */
    @GetMapping
    public String getUserProfile(Model model) {
        User user = userService.getCurrentLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("listOfComments", commentService.findAllByCustomer(user));
        return "customerPage";
    }

    /**
     * метод для формирования данных для обновления User.
     *
     * @param user  пользователь
     * @param model модель для view
     * @return
     */
    @PostMapping("/profile")
    public String updateUserProfile(User user, Model model) {
        User updateUser = userService.updateUserProfile(user);
        model.addAttribute("user", updateUser);

        return "redirect:/customer";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "changePassword";
    }

    /**
     * метод обработки изменения пароля User.
     *
     * @param model       модель для view
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     * @return страница User
     */
    @PostMapping("/change-password")
    public ResponseEntity changePassword(Model model,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        User user = userService.getCurrentLoggedInUser();
        if (userService.findById(user.getId()).isEmpty()) {
            log.debug("Нет пользователя с идентификатором: {}", user.getId());
            return ResponseEntity.noContent().build();
        }
        if (ValidationUtils.isNotValidEmail(user.getEmail())) {
            log.debug("Wrong email! Не правильно введен email");
            return ResponseEntity.badRequest().body("notValidEmailError");
        }
        if (!userService.findById(user.getId()).get().getEmail().equals(user.getEmail())
                && userService.isExist(user.getEmail())) {
            log.debug("Пользователь с таким адресом электронной почты уже существует");
            return ResponseEntity.badRequest().body("duplicatedEmailError");
        }
        if (userService.changePassword(user.getId(), oldPassword, newPassword))
            log.debug("Изменения для пользователя с идентификатором: {} был успешно добавлен", user.getId());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/activatenewmail/{token}")
    public String changeMail(Model model, @PathVariable String token, HttpServletRequest request) {
        userService.activateNewUsersMail(token, request);
        model.addAttribute("message", "Email address changes successfully");
        return "redirect:/customer";
    }
}
