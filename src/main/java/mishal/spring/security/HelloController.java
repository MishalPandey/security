package mishal.spring.security;

import mishal.spring.security.jwtWithValidation.JwtUtilsWithValidation;
import mishal.spring.security.jwtWithValidation.LoginRequestwithValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsWithValidation jwtUtils;

    @GetMapping("/hello")
    public String sayHello() {

        return "Hello, Mishal!";
    }

    @GetMapping("/admin/hello")
    public String sayAdminHello() {
        return "Hello, Admin Mishal!";
    }

    @GetMapping("/user/hello")
    public String sayUserHello() {
        return "Hello, User Mishal!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/viphello")
    public String sayVipHello() {
        return "Hello, Vip Mishal!";
    }

    // JWT based login(only jwt token generation) endpoint without jwt key validation
    @PostMapping("/signin")
    public String login(@RequestBody LoginRequestwithValidation loginRequest) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return "Not Authenticated: " + e.getMessage();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtTokenFromUsername(userDetails);

        return jwtToken;
    }
}

