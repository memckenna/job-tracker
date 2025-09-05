package com.jobtracker.graphql;

import com.jobtracker.model.User;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final UserRepository userRepository;
    private final CurrentUserProvider currentUser;

    @QueryMapping
    public User me() {
        String userId = currentUser.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
