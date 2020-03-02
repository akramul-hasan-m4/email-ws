package mail.atililimted.net.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

	private static final String RESOURCE_ID = "ati-mail-server-rest-api";


	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.antMatcher("/**").authorizeRequests()
		.antMatchers("/swagger-ui.html", "/swagger-resources/**", "/actuator/**", "/webjars/**", "/swagger/**", "/v2/**", "/", "/h2_console/**").permitAll()
		.anyRequest().authenticated();
		
		http.headers().frameOptions().disable();
	}
	
}
