package com.clinic.vetpet.configure.model;

/**
 * AuthenticationResponse model to send authenticate details
 * to client side
 *
 * @author Keshani
 * @since 2023/03/15
 */
public class AuthenticationResponse {

    private final String jwtToken;

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
