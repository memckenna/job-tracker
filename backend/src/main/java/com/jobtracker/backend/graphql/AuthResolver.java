package com.jobtracker.backend.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.jobtracker.backend.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthResolver {

    private final AuthService authService;

    @MutationMapping
    public String register(@Argument String email, @Argument String password) {
        return authService.register(email, password);
    }

    @MutationMapping
    public String login(@Argument String email, @Argument String password) {
        return authService.login(email, password);
    }


}
