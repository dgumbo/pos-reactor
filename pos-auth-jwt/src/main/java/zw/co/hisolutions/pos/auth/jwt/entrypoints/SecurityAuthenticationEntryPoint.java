package zw.co.hisolutions.pos.auth.jwt.entrypoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import zw.co.hisolutions.pos.auth.jwt.exceptions.ErrorMessage;
import zw.co.hisolutions.pos.auth.jwt.exceptions.RestErrorList;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import zw.co.hisolutions.pos.auth.jwt.entity.ResponseWrapper;

/**
 * SecurityAuthenticationEntryPoint is called by ExceptionTranslationFilter to handle all AuthenticationException.
 * These exceptions are thrown when authentication failed : wrong login/password, authentication unavailable, invalid token
 * authentication expired, etc.
 *
 * For problems related to access (roles), see RestAccessDeniedHandler.
 */
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        RestErrorList errorList = new RestErrorList(SC_UNAUTHORIZED, new ErrorMessage(authException.getMessage()));
        ResponseWrapper responseWrapper = new ResponseWrapper(null, singletonMap("status", SC_UNAUTHORIZED), errorList);
        ObjectMapper objMapper = new ObjectMapper();

        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
        wrapper.setStatus(SC_UNAUTHORIZED);
        wrapper.setContentType(APPLICATION_JSON_VALUE);
        wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
        wrapper.getWriter().flush();
    }
}
