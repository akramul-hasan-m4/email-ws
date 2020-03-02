package mail.atililimted.net.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */
@Component
public class CrossOriginFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(CrossOriginFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// We do not want to do anything here.
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

		log.info("Applying CORS filter");
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		// We do not want to do anything here.
	}
}
