package com.ruyuan.jiangzh.iot.user.temp;

import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class RestAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }


//
//    private UserService userService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        Assert.notNull(authentication, "No authentication data provided");
//
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof UserPrincipal)) {
//            throw new BadCredentialsException("Authentication Failed. Bad user principal.");
//        }
//
//        UserPrincipal userPrincipal =  (UserPrincipal) principal;
//        if (userPrincipal.getType() == UserPrincipal.Type.USER_NAME) {
//            String username = userPrincipal.getValue();
//            String password = (String) authentication.getCredentials();
//            return authenticateByUsernameAndPassword(userPrincipal, username, password);
//        } else {
//            return null;
//        }
//    }
//
//    private Authentication authenticateByUsernameAndPassword(UserPrincipal userPrincipal, String username, String password) {
//        User user = userService.selectByName(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found: " + username);
//        }
//
//        UserCredentials userCredentials = userService.findUserCredentialsByUserId(user.getId());
//        if (userCredentials == null) {
//            throw new UsernameNotFoundException("User credentials not found");
//        }
//
//        if (!userCredentials.isEnabled()) {
//            throw new DisabledException("User is not active");
//        }
//
//        if (!encoder.matches(password, userCredentials.getPassword())) {
//            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
//        }
//
//        if (user.getAuthority() == null) throw new InsufficientAuthenticationException("User has no authority assigned");
//
//        SecurityUser securityUser = new SecurityUser(user, userCredentials.isEnabled(), userPrincipal);
//
//        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
//    }
}
