package mail.atililimted.net.serviceimpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.IntStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import mail.atililimted.net.entity.Attachment;
import mail.atililimted.net.entity.BCC;
import mail.atililimted.net.entity.CC;
import mail.atililimted.net.entity.Mail;
import mail.atililimted.net.entity.MailType;
import mail.atililimted.net.entity.To;
import mail.atililimted.net.repo.MailRepo;
import mail.atililimted.net.service.MailService;

@Slf4j
public class MailServiceImpl implements MailService{
	
	private static final String SPLITTER = "_4f2q0-";
	
	@Autowired
	private MailRepo repo;
	
	@Resource(name = "properties")
	private Properties properties;
	
	@Resource
	private String uploadPath;

	@Value("${spring.mail.host}")
	private String host;

	@Value("${ati.store.protocol}")
	private String protocol;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Override
	public List<Mail> fetchAllUnreedMail() {
		List<Mail> mailList = new ArrayList<>();
		
		try {
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore(protocol);
			store.connect(host, username, password);
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			Message[] messages = emailFolder.getMessages();

			IntStream.range(0, messages.length).forEach(index -> {
				Message message = messages[index];
				Mail m = null;
				try {
					m = writePart(message);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
				if (m != null) {
					//m.setMailId(messages.length - index);
				}
				mailList.add(m);

			});

			emailFolder.close(true);
			store.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return mailList;
	}
	
	private Mail writePart(Part p) throws Exception {
		Mail mail = new Mail();
		List<Attachment> attachments = new ArrayList<>();
		if (p instanceof Message) {
			writeEnvelope((Message) p, mail);
		}

		if (p.isMimeType("text/plain") && p.getContent() != null) {

			System.out.println("msg ==>> " + (String) p.getContent());
			mail.setBody((String) p.getContent());

		} else if (p.isMimeType("multipart/*")) {

			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			String attachFiles = "";
			for (int i = 0; i < count; i++) {
				MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(i);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					Attachment attachment = new Attachment();
					
					String fileName = part.getFileName();
					attachment.setAttachmentName(fileName);
					
					attachments.add(attachment);
					// add here attachment data
					attachFiles += fileName + ", ";
					if (Files.exists(Paths.get(uploadPath + fileName))) {
						System.out.println("file Exists ==>> ");
					} else {
						part.saveFile(uploadPath + fileName);
					}
					if (fileName.equalsIgnoreCase("Account_chart_scanned.zip")) {
						Message message = (Message) p;
						message.setFlag(Flags.Flag.DELETED, true);
						System.out.println("Delete is called it will be change ==>> ");
					}

				}  else { 
					if (part.getContent() instanceof Multipart){
						String f = getTextFromMimeMultipart(part);
						String html = (String) part.getContent();
						System.out.println("msg 222222 ==>> " + html);
						mail.setBody(f);
					}
				
				
				  if (part.isMimeType("text/plain") && part.getContent() != null &&
				  !part.getContent().toString().isEmpty()) {
				  
				  System.out.println("msg 222222 ==>> " +(String) part.getContent());
				  mail.setBody((String) part.getContent());
				  
				  }
				  
				  
				 // writePart(part);
				 

				 }
			}
			if (!attachFiles.isEmpty()) {
				System.out.println("attachFiles ==>> " + attachFiles);
				//mail.setAttachmentName(attachFiles);
			}
			
			mail.setAttachmentList(attachments);
		}

		return mail;
	}

	public void writeEnvelope(Message m, Mail mail) throws Exception {

		Address[] a;
		List<To> toList = new ArrayList<>();
		List<CC> ccList = new ArrayList<>();
		List<BCC> bccList = new ArrayList<>();
		

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++) {
				mail.setFromAddress(a[j].toString());
			}
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++) {
				To to = new To();
				to.setToAddress(a[j].toString());
				toList.add(to);
				//mail.setTo(a[j].toString());
			}
		}
		
		// CC
		if ((a = m.getRecipients(Message.RecipientType.CC)) != null) {
			for (int j = 0; j < a.length; j++) {
				CC cc = new CC();
				cc.setCcAddress(a[j].toString());
				ccList.add(cc);
				System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
				//mail.setTo(a[j].toString());
			}
		}
		
		// BCC
		if ((a = m.getRecipients(Message.RecipientType.BCC)) != null) {
			for (int j = 0; j < a.length; j++) {
				BCC bcc = new BCC();
				bcc.setBccAddress(a[j].toString());
				bccList.add(bcc);
				//mail.setTo(a[j].toString());
			}
		}

		// SUBJECT
		if (m.getSubject() != null) {
			mail.setSubject(m.getSubject());
		}

		if (m.getSentDate() != null) {
			mail.setSentDate(m.getSentDate().toString());
		}
		
		mail.setToList(toList);
		mail.setCcList(ccList);
		mail.setBccList(bccList);

	}

	private String getTextFromMimeMultipart(Part p) throws Exception {
		String result = "";
		Multipart mimeMultipart = (Multipart) p.getContent();
		int partCount = mimeMultipart.getCount();
		for (int i = 0; i < partCount; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				// result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
				result = html;
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((Part) bodyPart.getContent());
			}
		}
		return result;
	}

	@Override
	public String sendMail(Mail mail) {
		
		Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
					@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
		try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            
            int toSize = mail.getToList() == null ? 0 : mail.getToList().size();
            int ccSize = mail.getCcList() == null ? 0 : mail.getCcList().size();
            int bccSize = mail.getBccList() == null ? 0 : mail.getBccList().size();
            
            InternetAddress[] toAddress = new InternetAddress[toSize];
            InternetAddress[] ccAddress = new InternetAddress[ccSize];
            InternetAddress[] bccAddress = new InternetAddress[bccSize];
            
            IntStream.range(0, toSize).forEach(index -> {
            	
				try {
					toAddress[index] = new InternetAddress(mail.getToList().get(index).getToAddress());
					message.addRecipient(Message.RecipientType.TO, toAddress[index]);
				} catch (MessagingException e) {
					log.error(e.getMessage());
				}
				
            });
            
			IntStream.range(0, ccSize).forEach(index -> {

				try {
					ccAddress[index] = new InternetAddress(mail.getCcList().get(index).getCcAddress());
					message.addRecipient(Message.RecipientType.CC, ccAddress[index]);
				} catch (MessagingException e) {
					log.error(e.getMessage());
				}

			});
			IntStream.range(0, bccSize).forEach(index -> {

				try {
					bccAddress[index] = new InternetAddress(mail.getBccList().get(index).getBccAddress());
					message.addRecipient(Message.RecipientType.BCC, bccAddress[index]);
				} catch (MessagingException e) {
					log.error(e.getMessage());
				}

			});
       
			message.setSubject(mail.getSubject());
            List<String> fileList = new ArrayList<>();
            
            if(mail.getMailType().equals(MailType.SIMPLE)) {
            	message.setText(mail.getBody());
            	
            }else if(mail.getMailType().equals(MailType.MUTIPART)) {
            	
            	 Multipart multipart = new MimeMultipart();
            	 multipartMailHandler(mail, fileList, multipart);
                 message.setContent(multipart);
            	
            }else if(mail.getMailType().equals(MailType.EMBEDDED)) {
               
                MimeMultipart multipart = new MimeMultipart("related");
                multipartMailHandler(mail, fileList, multipart);
                message.setContent(multipart);
            }
   
            // Send message
            Transport.send(message);
            repo.save(mail);
            
            fileList.parallelStream().filter(Objects::nonNull).forEach(file ->{
            	try {
            		String orginalFilePath = uploadPath + file;
					Path filePath = Paths.get(orginalFilePath);
					Files.delete(filePath);
				} catch (IOException e) {
					log.error(e.getMessage());
				}
            });

            log.info("Sent mail successfully....");

        } catch ( MessagingException e) {
        	log.error(e.getMessage());
        	return "Mail Send Failed Cause ==>> " + e.getMessage();
        }
		
		return "Mail Send is Success";
	}

	private void multipartMailHandler(Mail mail, List<String> fileList, Multipart multipart) throws MessagingException {
		if(!fileList.isEmpty()) {
			fileList.clear();
		}
		 BodyPart messageBodyPart = new MimeBodyPart();
		 messageBodyPart.setContent(mail.getHtmlBody(), "text/html; charset=UTF-8");
		 multipart.addBodyPart(messageBodyPart);
		 
		 mail.getAttachmentList().stream().filter(Objects::nonNull).forEach(file -> {
			try {
				byte[] decodedImg = Base64.getDecoder().decode(file.getAttachmentData().getBytes(StandardCharsets.UTF_8));
				String fileName = System.currentTimeMillis() + SPLITTER + file.getAttachmentName();
				Path destinationFile = Paths.get(uploadPath, fileName);
				Files.write(destinationFile, decodedImg);
				fileList.add(fileName);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		 });
		 
		 int imgEm = 0;
		 
		 for (String file : fileList) {
			String orginalFilePath = uploadPath + file;
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(orginalFilePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			
			if(mail.getMailType().equals(MailType.EMBEDDED)){
				 messageBodyPart.setHeader("Content-ID", "<"+"image"+imgEm+">"); // kaj korte hobe
			}else if(mail.getMailType().equals(MailType.MUTIPART)) {
				String fileName = file.split(SPLITTER)[1];
				messageBodyPart.setFileName(fileName);
			}
			imgEm++;
			
			multipart.addBodyPart(messageBodyPart);
			
		 }
	}

}
