package mishal.spring.security;


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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")


@Configuration  // 0. Enable configuration class
@EnableWebSecurity // 0. Enable web security
@EnableMethodSecurity // 3. to enable method level security using annotations like @PreAuthorize
// which we had used in HelloController with method sayVipHello()
// esase hum bina kuch kiya @PreAuthorize("hasRole('VIP')") laga ke method level pe role based security laga sakte hai
// bina securityfilterchain me kuch define kiye


public class SecurityConfig {
    // 1. Define an in-memory user details service without roles
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//        http.authorizeHttpRequests(authorizedRequests ->
//                authorizedRequests.anyRequest().authenticated());
//        http.httpBasic(Customizer.withDefaults());
//    return http.build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.withUsername("user1")
//                .password("{noop}password1") // {noop} indicates that no encoding is used
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password("{noop}adminPassword")
//                .build();
//
//        UserDetails user2 = User.withUsername("user2")
//                .password("{noop}password2")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, admin, user2);
//    }

    //2. Define an in-memory user details service with roles
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
                .password("{noop}password1") // {noop} indicates that no encoding is used
                .roles("USER") // Internally ROLE_USER aise stored karta hai spring security, roles function ko visit karlo
                // samjhne ke liye
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}adminPassword")
                .roles("ADMIN") // ROLE_ADMIN
                .build();

        UserDetails user2 = User.withUsername("user2")
                .password("{noop}password2")
                .roles("USER") // ROLE_USER
                .build();

        return new InMemoryUserDetailsManager(user1, admin, user2);

        // result
        // for this http://localhost:8080/admin/hello endpoint only admin can access
        // and while accessing with user1 or user2 it will give 403 forbidden error
        // kyuki /admin/** endpoint sirf ROLE_ADMIN wale hi access kar sakte hai jo humnne securityfilterchain me define kiya hai


    }
}

