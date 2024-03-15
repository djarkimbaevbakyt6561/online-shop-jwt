package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.entities.User;
import peaksoft.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAPI {
    private final UserService userService;

    @PutMapping("/{userID}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public SimpleResponse updateUser(@PathVariable Long userID, @RequestBody User user, Principal principal){
        return userService.update(principal, userID, user);
    }

}