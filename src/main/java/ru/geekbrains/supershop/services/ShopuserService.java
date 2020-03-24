package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.persistence.repositories.ShopuserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ShopuserService implements UserDetailsService {

    private final ShopuserRepository shopuserRepository;

    public Shopuser findByPhone(String phone) {
        return shopuserRepository.findOneByPhone(phone);
    }

    public Shopuser getAnonymousUser() {
        return shopuserRepository.findOneByPhone("anonymous");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Shopuser shopuser = shopuserRepository.findOneByPhone(username);
        if (shopuser == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new User(shopuser.getPhone(), shopuser.getPassword(), mapRolesToAuthorities(shopuser.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return role != null ?
                new ArrayList<GrantedAuthority>() {{ add((GrantedAuthority) role::name); }} :
                new ArrayList<>();
    }

    public boolean isUserExist(String phone) {
        return shopuserRepository.existsByPhone(phone);
    }

}