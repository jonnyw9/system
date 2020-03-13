package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Constraint.UserValidation;
import com.tutorial.booking.system.Repository.CalendarRepository;
import com.tutorial.booking.system.Service.EmailSender;
import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.NotificationService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Calendar;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/")
public class FrontController {

    private UserDto user;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserValidation userValidation;

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

        String url = "http://localhost:8080/api/event/getall/" + String.valueOf(userId);

        User userById = userService.getUserById(userId);

        String dayStart = "";
        String dayEnd = "";

        if(user.isStudent() && !user.isStaff()){
            dayStart = "06:00";
            dayEnd = "20:00";
        }else if(user.isStaff()){
            dayStart = Objects.requireNonNull(calendarRepository.findById(userById.getCalendarId()
                    .getCalendarId()).orElse(null)).getDayStartTime().toLocalTime().toString();
            dayEnd = Objects.requireNonNull(calendarRepository.findById(userById.getCalendarId()
                    .getCalendarId()).orElse(null)).getDayEndTime().toLocalTime().toString();
        }


        model.addAttribute("dayStart", dayStart);


        model.addAttribute("dayEnd", dayEnd);
        model.addAttribute("staff", user);

        model.addAttribute("url", url);

        String name = "";

        model.addAttribute("name", name);

        List<Notification> notifications = notificationService.getUserNotifications(userId);

        int unreadNotifications = 0;

        for (int i = 0; i < notifications.size() ; i++) {
            if(!notifications.get(i).isSeen()){
                unreadNotifications++;
            }
        }

        model.addAttribute("unreadNotifications", unreadNotifications);

        return "home";
    }

    @GetMapping("search")
    public String searchStaff(@RequestParam(value = "name") Optional<String> name,
                              //@RequestParam(value = "page") Optional<Integer> page,
                              Model model,
                              Authentication authentication){

        UserDto user = userService.makeUserDto(authentication);
        model.addAttribute("name", "");
        try{
            String nameReplace;

            nameReplace = name.orElse("_").replace("+", "");

            model.addAttribute("searchResults",
                    userService.listUserByName(nameReplace));

        }catch (Exception e){
            model.addAttribute("searchResults",
                    userService.listUserByName(name.orElse("_")));
        }
        return "searchStaff";
    }

    @GetMapping("register")
    public String register(Model model){

        if(!model.containsAttribute("user")){
            UserDto userDto = new UserDto();
            model.addAttribute("user", userDto);
        }
        return "register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        bindingResult = userValidation.validate(userDto, bindingResult);

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event",
                    bindingResult);
            redirectAttributes.addFlashAttribute("user", userDto);
            return "redirect:/register";
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


    /*
    @GetMapping("staff/times/{id}")
    public String getStaffTime(@PathVariable("id") int id, Model model){

        User user = userService.getUserById(id);

        model.addAttribute("staff", user);

        List<Timestamp> availableTimes = eventService.calulateFreeTimeForTheWeek(user.getCalendarId());

        List<LocalDateTime> availableDates = new ArrayList<>();


        for(Timestamp timestamp: availableTimes){
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            availableDates.add(localDateTime);
        }


        model.addAttribute("dates", availableDates);

        return "displayAvailableTimes";
    }
    */

    @GetMapping("staff/times/{id}")
    private String getStaffTime(@PathVariable("id") int id, Model model){
        User user = userService.getUserById(id);
        String dayStart =
                Objects.requireNonNull(calendarRepository.findById(user.getCalendarId().
                        getCalendarId()).orElse(null)).getDayStartTime().toLocalTime().toString();
        model.addAttribute("dayStart", dayStart);

        String dayEnd =
                Objects.requireNonNull(calendarRepository.findById(user.getCalendarId()
                        .getCalendarId()).orElse(null)).getDayEndTime().toLocalTime().toString();
        model.addAttribute("dayEnd", dayEnd);
        model.addAttribute("staff", user);
        String url = "http://localhost:8080/api/event/getall/" + String.valueOf(id);
        model.addAttribute("url", url);

        return "staffCalendar";
    }


}
