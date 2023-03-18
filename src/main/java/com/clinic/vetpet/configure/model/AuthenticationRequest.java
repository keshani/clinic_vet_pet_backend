package com.clinic.vetpet.configure.model;

import lombok.Getter;
import lombok.Setter;

/**
 * AuthenticationRequest model to get authenticate details
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Getter
@Setter
public class AuthenticationRequest {

    private String username;
    private String password;

    public AuthenticationRequest() {
    }
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
