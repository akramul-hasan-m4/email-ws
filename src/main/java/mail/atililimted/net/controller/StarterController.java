package mail.atililimted.net.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarterController {
	
	@GetMapping("/")
	public String init() {
		return "Welcome To ATI Mail Client Server - Version :: " + getClass().getPackage().getImplementationVersion();
	}

}
