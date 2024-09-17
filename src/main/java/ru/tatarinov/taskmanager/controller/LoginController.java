package ru.tatarinov.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tatarinov.taskmanager.DTO.LoginDTO;
import ru.tatarinov.taskmanager.DTO.UserDTO;
import ru.tatarinov.taskmanager.exception.AuthenticationFailException;
import ru.tatarinov.taskmanager.security.JwtResponse;
import ru.tatarinov.taskmanager.security.JwtTokenProvider;
import ru.tatarinov.taskmanager.security.UserDetailsServiceImpl;
import ru.tatarinov.taskmanager.service.UserServiceImp;
import ru.tatarinov.taskmanager.util.BindingResultValidation;
import ru.tatarinov.taskmanager.validation.RegistrationValidation;

@RestController
@Tag(name = "Login")
@RequestMapping(value = "/login", consumes = {"application/JSON"}, produces = {"application/JSON", "application/XML"})
public class LoginController {
    private final UserServiceImp userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegistrationValidation registrationValidation;

    public LoginController(UserServiceImp userService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider, RegistrationValidation registrationValidation) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registrationValidation = registrationValidation;
    }


    @Operation(summary = "Login", description = "Login to system and receive JWT token")
    @PostMapping()
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginDTO loginDTO, BindingResult bindingResult){
        BindingResultValidation.bindingResultCheck(bindingResult);

        String username = loginDTO.getMail();

        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getMail(), loginDTO.getPassword());
        JwtResponse jwtResponse = null;
        try {
            Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
            if (authenticationResponse.isAuthenticated()){
                sc.setAuthentication(authenticationResponse);
                UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getMail());
                jwtResponse = new JwtResponse(jwtTokenProvider.createToken(userDetails));
            }
        }
        catch (AuthenticationException e){
            throw new AuthenticationFailException("Mail or password is incorrect");
        }
        return ResponseEntity.ok(jwtResponse);
    }

    @Operation(summary = "Registration new user", description = "Registers new user")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        registrationValidation.validate(userDTO, bindingResult);

        BindingResultValidation.bindingResultCheck(bindingResult);

        userService.createUser(userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
