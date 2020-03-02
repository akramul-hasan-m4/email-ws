package mail.atililimted.net.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mail.atililimted.net.model.UserInfo;


@Service
public interface UserService {

	@Transactional(readOnly = true)
	UserInfo findUserByUsername(String username);
	
}
