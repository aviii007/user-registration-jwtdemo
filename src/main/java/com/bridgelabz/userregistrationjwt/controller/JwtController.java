package com.bridgelabz.userregistrationjwt.controller;

import com.bridgelabz.userregistrationjwt.helper.JwtUtil;
import com.bridgelabz.userregistrationjwt.model.JwtRequest;
import com.bridgelabz.userregistrationjwt.model.JwtResponse;
import com.bridgelabz.userregistrationjwt.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials!!");
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials!!");
        }


        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(jwtRequest.getUserName());

        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT " + token);


        return ResponseEntity.ok(new JwtResponse(token));
    }
}