package mail.atililimted.net.serviceimpl;




import mail.atililimted.net.model.UserInfo;
import mail.atililimted.net.service.UserService;

public class UserServiceImpl implements UserService{
	
	//@Autowired private UserRepo repo;

	@Override
	public UserInfo findUserByUsername(String username) {
		UserInfo u = new UserInfo();
		u.setUsername(username);
		u.setPassword("1234");
		u.setRoleName("Admin");
		return u;
	}

}
