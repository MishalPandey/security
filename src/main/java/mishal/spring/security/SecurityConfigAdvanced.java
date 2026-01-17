package mishal.spring.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
@ConditionalOnProperty(prefix = "securityAdv", name = "enabled", havingValue = "true")



@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfigAdvanced {

    @Autowired
    DataSource dataSource;

    //1. Use database for roles
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authorizedRequests ->
                authorizedRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.withUsername("user1")
                //.password("{noop}password11") //noop means no encoding and in database it will be stored as nooppassword11
                .password(passwordEncoder().encode("Password11")) // bcrypt encoding and in database it will be stored as encoded string
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                //.password("{noop}adminPasswordd")
                .password(passwordEncoder().encode("adminPasswordd"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.withUsername("user2")
                //.password("{noop}password22")
                .password(passwordEncoder().encode("password22"))
                .roles("USER")
                .build();

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(admin);
        userDetailsManager.createUser(user2);
        return userDetailsManager;

    }

    /*
    -- SQL
        CREATE TABLE public.users (
          username VARCHAR(50) PRIMARY KEY,
          password VARCHAR(500) NOT NULL,
          enabled BOOLEAN NOT NULL
        );

        CREATE TABLE public.authorities (
          username VARCHAR(50) NOT NULL,
          authority VARCHAR(500) NOT NULL,
          CONSTRAINT fk_authorities_users
            FOREIGN KEY (username) REFERENCES public.users(username)
              ON UPDATE CASCADE ON DELETE CASCADE
        );


        CREATE UNIQUE INDEX ix_auth_username
          ON public.authorities (username, authority);

     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

