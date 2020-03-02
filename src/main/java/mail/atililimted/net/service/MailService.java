package mail.atililimted.net.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import mail.atililimted.net.entity.Mail;

@Service
@Transactional
public interface MailService {

	 List<Mail> fetchAllUnreedMail();
	 
	 String sendMail(Mail mail);
	 
}
