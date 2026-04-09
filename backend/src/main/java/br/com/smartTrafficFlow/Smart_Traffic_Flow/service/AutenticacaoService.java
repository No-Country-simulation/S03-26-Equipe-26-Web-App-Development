package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.User;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.UserRepository;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new SecurityUser(usuario);
    }
}
