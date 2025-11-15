package com.flightreservation.flightreservation.controllers;

import com.flightreservation.flightreservation.model.User;
import com.flightreservation.flightreservation.exceptions.UserAlreadyRegisteredException;
import com.flightreservation.flightreservation.exceptions.UserNotFoundException;
import com.flightreservation.flightreservation.repositories.UserRepository;
import com.flightreservation.flightreservation.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          SecurityService securityService){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityService = securityService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/showReg")
    public String showRegistrationPage() {
        LOGGER.info("Inside showRegistrationPage()");
        return "login/registerUser";
    }

    @PostMapping("/registerUser")
    public String register(@ModelAttribute("user") User user) {

        LOGGER.info("{} Inside register()", user.getEmail());

        User foundUser= userRepository.findByEmail(user.getEmail())
                .orElseThrow(()->new UserAlreadyRegisteredException("User already exist"));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "login/login";
    }

    @GetMapping("/showLogin")
    public String showLoginPage() {
        LOGGER.info("Inside showLoginPage()");
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        ModelMap modelmap){

        LOGGER.info("{} Inside login()",email);
        User foundUser=userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        boolean loginResponse = securityService.login(email, password);
        if(loginResponse) {
            modelmap.addAttribute("msg","Successfully logged in");
            return "flights/findFlights";
        } else {
            LOGGER.info("User entered Invalid credentials email:{} and password:{}",email,password);
            modelmap.addAttribute("msg","Invalid username or password");
        }
        return "login/login";
    }

}
