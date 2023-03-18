package com.clinic.vetpet.configure.security;

import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is to retrive user information for authentication
 *
 * @author Keshani
 * @since 2023/03/15
 */

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).get();
        return new AppUserDetail(user);
    }
}

