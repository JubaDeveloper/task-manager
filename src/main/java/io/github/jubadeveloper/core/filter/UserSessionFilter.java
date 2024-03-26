package io.github.jubadeveloper.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

/**
 * Used for inject user information into the session attribute
 * for further user by others component
 * */
@Component
public class UserSessionFilter extends OncePerRequestFilter {
    private static final String PATH_PATTERN = "/panel/.*";
    private static final Logger logger = LogManager.getLogger(UserSessionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("Inspecting path to inject user - {}", request.getPathInfo());
        setAuth(request);
        filterChain.doFilter(request, response);
    }

    /**
     * Get the Authentication from the SecurityContextHolder if there is any
     * and set it into request
     * @param httpServletRequest The current request to be inspected
     */
    private void setAuth (HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getPathInfo().matches(PATH_PATTERN)) {
            UserDetails authentication = Optional.of(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .map(UserDetails.class::cast)
                    .orElse(null);
            logger.info("Injecting user into auth into request");
            httpServletRequest
                    .getSession()
                    .setAttribute("user", authentication);
        }
    }
}
