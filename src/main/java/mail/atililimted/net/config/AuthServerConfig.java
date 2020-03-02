package mail.atililimted.net.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import mail.atililimted.net.serviceimpl.UserDetailsServiceImpl;


/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Value("${basic.auth.client.id}")
	private String clientId;
	
	@Value("${basic.auth.client.secret}")
	private String clientSecret;

	@Autowired UserDetailsServiceImpl userDetailsService;
	
	@Autowired BCryptPasswordEncoder passwordEncoder;

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
		.inMemory()
		.withClient(clientId)
		.secret(passwordEncoder.encode(clientSecret))
		.authorizedGrantTypes("password", "authorization_code", "refresh_token")
		.scopes("read", "write", "trust")
		.accessTokenValiditySeconds(3600)
		.autoApprove(true);
	}

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.tokenStore(tokenStore())
		.authenticationManager(authenticationManager)
		.accessTokenConverter(defaultAccessTokenConverter())
		.userDetailsService(userDetailsService);
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(defaultAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter defaultAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("123");
		return converter;
	}

}
