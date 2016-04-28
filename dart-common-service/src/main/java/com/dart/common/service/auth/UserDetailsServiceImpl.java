package com.dart.common.service.auth;


import com.dart.data.domain.Identity;
import com.dart.data.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author RMPader
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Identity identity = identityRepository.findIdentityFromProvider(username, "basic");
        return new UserDetailsImpl(identity);
    }
}
