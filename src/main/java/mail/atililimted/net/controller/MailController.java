package mail.atililimted.net.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mail.atililimted.net.entity.Mail;
import mail.atililimted.net.service.MailService;

@RestController
public class MailController {

	private @Autowired MailService mailService;

	@GetMapping("/receiveMail")
	public List<Mail> getUnreadMail() {
		return mailService.fetchAllUnreedMail();
	}
	
	@PostMapping("/send-mail")
	public ResponseEntity<String> sendMail(@RequestBody Mail mail) {
		if(mail == null) return new ResponseEntity<>("Mail Not sent Because Mail is empty", HttpStatus.OK);
		if(mail.getMailType() == null) return new ResponseEntity<>("Please add mail Type", HttpStatus.OK);
		 
		return new ResponseEntity<>(mailService.sendMail(mail), HttpStatus.OK);
	}
	
	

}
