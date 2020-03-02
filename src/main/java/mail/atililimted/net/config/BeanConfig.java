package mail.atililimted.net.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mail.atililimted.net.service.MailService;
import mail.atililimted.net.service.UserService;
import mail.atililimted.net.serviceimpl.MailServiceImpl;
import mail.atililimted.net.serviceimpl.UserServiceImpl;


/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */
@Configuration
public class BeanConfig {

	@Bean
	public UserService userBean() {
		return new UserServiceImpl();
	}
	
	@Bean
	public MailService mailBean() {
		return new MailServiceImpl();
	}
}
