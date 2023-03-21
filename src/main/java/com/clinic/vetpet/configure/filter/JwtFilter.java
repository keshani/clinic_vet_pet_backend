package com.clinic.vetpet.configure.filter;

import com.clinic.vetpet.common.util.GlobalConstants;
import com.clinic.vetpet.configure.security.AppUserDetailsService;
import com.clinic.vetpet.configure.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * JWTFilter to filter all the requests except whitlised urls
 *
 * @author Keshani
 * @since 2023/03/15
 */

@Service
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    AppUserDetailsService myUserDetailsService;
    @Autowired
    JWTUtil jWTUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationToken  = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
            jwtToken = authorizationToken.substring(7);
            username =jWTUtil.extractUsername(jwtToken);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detail = myUserDetailsService.loadUserByUsername(username);
            if (jWTUtil.validateToken(jwtToken, detail)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        Set<String> searchStrings = GlobalConstants.WHITE_LIST_URL;
        // Pass JWTfilter check for white listed urls
        return searchStrings.stream().anyMatch(path::contains);
    }

}
