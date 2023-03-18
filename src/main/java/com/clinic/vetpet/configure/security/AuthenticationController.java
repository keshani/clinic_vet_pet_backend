package com.clinic.vetpet.configure.security;

import com.clinic.vetpet.configure.model.AuthenticationRequest;
import com.clinic.vetpet.configure.model.AuthenticationResponse;
import com.clinic.vetpet.configure.util.JWTUtil;
import com.clinic.vetpet.common.controller.BaseConroller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthenticationController extends BaseConroller {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AppUserDetailsService userDetailsService;
    @Autowired
    JWTUtil jWTUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword() )
            );
        } catch (Exception ex) {
             throw new RuntimeException("error", ex);
        }
        final UserDetails userDetil = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwtToken = jWTUtil.generateToken(userDetil);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
