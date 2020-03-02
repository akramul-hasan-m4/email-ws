package mail.atililimted.net.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailModel {

	private Integer mailId;
	private String subject;
	private String from;
	private String to;
	private String sentDate;
	private String attachmentName;
	private String messageBody;
}
