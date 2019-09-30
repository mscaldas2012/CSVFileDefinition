package gov.cdc.nccdphp.esurveillance;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private static final String ORIGIN = "*";
    private static final String ALLOW_CREDENTIALS = "true";
    private static final String ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
    private static final String MAX_AGE = "3600";
    private static final String ALLOW_HEADERS = "Content-Type, Accept, X-Requested-With, remember-me";

    public SimpleCORSFilter() {
        //log.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", ORIGIN);
        response.setHeader("Access-Control-Allow-Credentials", ALLOW_CREDENTIALS);
        response.setHeader("Access-Control-Allow-Methods", ALLOW_METHODS);
        response.setHeader("Access-Control-Max-Age", MAX_AGE);
        response.setHeader("Access-Control-Allow-Headers", ALLOW_HEADERS);

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}

