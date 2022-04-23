package com.ruyuan.jiangzh.iot.user.infrastructure.auth;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderAuthenticationPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher matchers;

    public HeaderAuthenticationPathRequestMatcher(List<String> matchPaths){
        List<RequestMatcher> matchs = matchPaths.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(matchs);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if(matchers.matches(request)){
            return true;
        }
        return false;
    }
}
