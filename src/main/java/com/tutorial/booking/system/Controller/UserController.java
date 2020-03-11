package com.tutorial.booking.system.Controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tutorial.booking.system.Repository.PasswordRepository;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/user/")
public class UserController {

    private final String userTemplatePrefix = "user/";

    private final String editPrefix = "edit/";

    private UserDto user;

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    UserService userService;

    @GetMapping("view")
    public String viewAccount(Authentication authentication, Model model){
        user = userService.makeUserDto(authentication);
        System.out.println(user.toString());

        model.addAttribute("user", user);

        return userTemplatePrefix + "viewUser";
    }

    @GetMapping(editPrefix + "delete")
    public String deleteAccount(){

        userService.deleteUserAccount(user.getUserId());

        return "redirect:/?deleted";
    }

    @GetMapping("edit/{editRequest}")
    public String editDetails(@PathVariable String editRequest, Model model){
        model.addAttribute("user", user);

        if(editRequest == null){
            return "redirect:/user/view?badEditRequest";
        }else{
            switch (editRequest) {
                case "name":
                    return userTemplatePrefix + "editName";
                case "email":
                    return userTemplatePrefix + "editEmail";
                case "password":
                    model.addAttribute("password", new PasswordDto());
                    return userTemplatePrefix + "editPassword";
                default:
                    return "redirect:/user/view?badEditRequest";
            }
        }
    }

    @PostMapping("edit/{editRequest}")
    public String editDetails(@PathVariable String editRequest, @ModelAttribute UserDto userDto, Model model,
                              BindingResult bindingResult){
        if (editRequest == null){
            return "redirect:/user/view?badEditRequest";
        }

        userService.updateDetails(userDto, editRequest);

        if(editRequest.equals("email")){
            return "redirect:/login?success";
        }

        return "redirect:/user/view?updated";
    }

    @PostMapping("edit/password")
    public String editPassword(@ModelAttribute PasswordDto passwordDto, Model model,
                               BindingResult bindingResult){


        Password password = passwordRepository.getOne(
                userService.getUserById(user.getUserId()).getPassword().getPasswordId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(passwordDto.getCurrentPassword(), password.getPassword())){
            return "redirect:/user/edit/password?badPass";
        }

        user.setPassword(passwordDto.getPassword());
        user.setConfirmPassword(passwordDto.getConfirmPassword());

        userService.updateDetails(user, "password");

        return "redirect:/login?success";
    }

}
