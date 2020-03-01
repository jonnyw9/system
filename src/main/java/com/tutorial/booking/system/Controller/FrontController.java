package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/")
public class FrontController {

    private UserDto user;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @GetMapping("admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping(value = "home", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Authentication authentication, Model model){
        user = userService.makeUserDto(authentication);

        int userId = user.getUserId();
        List<Event> events = eventService.getEventsForUser(userId);

        model.addAttribute("events", events);

        model.addAttribute("user", user);

        String lastname = "";

        model.addAttribute("lastname", lastname);

        return "home";
    }

    @GetMapping("search")
    public String searchStaff(@RequestParam (value = "lastname", required = false) String lastname, Model model){

        model.addAttribute("searchResults", userService.listUserByLastName(lastname));

        return "searchStaff";
    }

    @GetMapping("register")
    public String register(Model model){

        UserDto userDto = new UserDto();

        model.addAttribute("user", userDto);

        return "register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult){

        User existing = userService.getUserByEmail(userDto.getEmail());
        if(existing != null){
            bindingResult.rejectValue("email", null,
                    "There is already an account registered to that email address!");
        }

        if(bindingResult.hasErrors()){
            return "register";
        }

        userService.saveNewUser(userDto);

        return "redirect:/login?registered";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            user = null;
        }
        return "redirect:/login?logout";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }


    @GetMapping("error")
    public String error(){
        return "login";
    }

    @GetMapping("staff/times/{id}")
    public String getStaffTime(@PathVariable("id") int id, Model model){

        User user = userService.getUserById(id);

        model.addAttribute("staff", user);

        List<Timestamp> availableTimes = eventService.calulateFreeTimeForTheWeek(user.getCalendarId());

        List<EventDto> eventDtos = new ArrayList<>();


        for(Timestamp timestamp: availableTimes){
            EventDto eventDto = new EventDto();
            eventDto.setEventStartTimeStamp(timestamp);
            eventDtos.add(eventDto);
        }


        model.addAttribute("events", eventDtos);

        return "displayAvailableTimes";
    }


}
