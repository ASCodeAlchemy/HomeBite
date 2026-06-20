package com.homebite.provider_service.Config;

import com.homebite.provider_service.Entity.Provider;
import com.homebite.provider_service.Repositories.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final ProviderRepo providerRepo;

    @Autowired
    public MyUserDetailService(ProviderRepo providerRepo) {
        this.providerRepo = providerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Provider provider = providerRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new org.springframework.security.core.userdetails.User(
                provider.getEmail(),
                "",
                Collections.emptyList()
        );
    }
}
