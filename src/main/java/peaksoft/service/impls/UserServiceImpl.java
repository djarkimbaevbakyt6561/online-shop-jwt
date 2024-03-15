package peaksoft.service.impls;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.config.jwt.JwtService;
import peaksoft.dto.request.SignInRequest;
import peaksoft.dto.request.SignUpRequest;
import peaksoft.dto.response.RegisterResponse;
import peaksoft.dto.response.SignResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.enums.Role;
import peaksoft.entities.User;
import peaksoft.repository.UserRepository;
import peaksoft.service.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostConstruct
    private void saveAdmin() {
        userRepo.save(
                User.builder()
                        .name("Admin")
                        .email("admin@gmail.com")
                        .role(Role.ADMIN)
                        .password(passwordEncoder.encode("admin123"))
                        .build()
        );

    }
    @PostConstruct
    private void saveClient() {
        userRepo.save(
                User.builder()
                        .name("Client")
                        .email("user@gmail.com")
                        .role(Role.CLIENT)
                        .password(passwordEncoder.encode("client123"))
                        .build()
        );

    }

    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        boolean exists = userRepo.existsByEmail(signUpRequest.getEmail());
        if (exists) throw new RuntimeException("Email : " + signUpRequest.getEmail() + " already exist");
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword()))
            throw new RuntimeException("Invalid password");


        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.CLIENT);
        userRepo.save(user);

        String newToken = jwtService.createToken(user);
        return RegisterResponse.builder()
                .token(newToken)
                .simpleResponse(
                        SimpleResponse.builder()
                                .httpStatus(HttpStatus.OK)
                                .message("Successfully saved")
                                .build())
                .build();

    }

    @Override
    public SignResponse signIn(SignInRequest signInRequest) {
        User user = userRepo.findByEmail(signInRequest.email()).orElseThrow(() ->
                new NoSuchElementException("User with email: " + signInRequest.email() + " not found!"));

        String encodePassword = user.getPassword();
        String password = signInRequest.password();

        boolean matches = passwordEncoder.matches(password, encodePassword);

        if (!matches) throw new RuntimeException("Invalid password");


        String token = jwtService.createToken(user);
        return SignResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse update(Principal principal, Long userID, User user) {
        String email = principal.getName();
        User loginUser = userRepo.getByEmail(email);
        User realUser = userRepo.findById(userID).orElse(null);

        if (loginUser.getRole().equals(Role.ADMIN) || loginUser.getId().equals(userID)) {
            if (realUser != null) {
                realUser.setName(user.getName());
            }
        }else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).build();
    }


}