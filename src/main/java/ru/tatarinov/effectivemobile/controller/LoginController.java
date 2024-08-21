package ru.tatarinov.effectivemobile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.effectivemobile.DTO.LoginDTO;
import ru.tatarinov.effectivemobile.DTO.UserDTO;
import ru.tatarinov.effectivemobile.exception.AuthenticationFailException;
import ru.tatarinov.effectivemobile.model.User;
import ru.tatarinov.effectivemobile.security.JwtResponse;
import ru.tatarinov.effectivemobile.security.JwtTokenProvider;
import ru.tatarinov.effectivemobile.security.UserDetailsServiceImpl;
import ru.tatarinov.effectivemobile.service.UserService;
import ru.tatarinov.effectivemobile.util.BindingResultValidation;
import ru.tatarinov.effectivemobile.validation.RegistrationValidation;

@RestController
@Tag(name = "Login")
@RequestMapping(value = "/login", consumes = {"application/JSON"}, produces = {"application/JSON", "application/XML"})
public class LoginController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegistrationValidation registrationValidation;

    public LoginController(UserService userService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider, RegistrationValidation registrationValidation) {
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

        ModelMapper requestMapper = new ModelMapper();
        User user = requestMapper.map(userDTO, User.class);
        userService.createUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
