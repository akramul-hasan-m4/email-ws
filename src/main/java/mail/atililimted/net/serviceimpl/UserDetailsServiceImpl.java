package mail.atililimted.net.serviceimpl;




import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mail.atililimted.net.model.UserInfo;
import mail.atililimted.net.service.UserService;

/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired private UserService service;
	
	@Autowired BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserInfo userInfo1 = service.findUserByUsername(userName);
		String userRole = userInfo1 == null || userInfo1.getRoleName() == null ? "" : "ROLE_" + userInfo1.getRoleName();
		GrantedAuthority authority = new SimpleGrantedAuthority(userRole);
		return new User(userInfo1.getUsername(), passwordEncoder.encode(userInfo1.getPassword()), Arrays.asList(authority));
	}
	
}
