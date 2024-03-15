package peaksoft.service;

import peaksoft.dto.request.SignInRequest;
import peaksoft.dto.request.SignUpRequest;
import peaksoft.dto.response.RegisterResponse;
import peaksoft.dto.response.SignResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.entities.User;

import java.security.Principal;

/**
 * @author Mukhammed Asantegin
 */
public interface UserService {
    RegisterResponse signUp(SignUpRequest signUpRequest);

    SignResponse signIn(SignInRequest signInRequest);

    SimpleResponse update(Principal principal, Long userID, User user);
}