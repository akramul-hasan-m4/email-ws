package mail.atililimted.net.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger mailId;
	private String fromAddress;
	private String subject;
	private String sentDate;
	private String body;
	private String htmlBody;
	private MailType mailType;
	
	@OneToMany(mappedBy="toMail")
	private List<To> toList;
	
	@OneToMany(mappedBy="ccMail")
	private List<CC> ccList;
	
	@OneToMany(mappedBy="bccMail")
	private List<BCC> bccList;
	
	@OneToMany(mappedBy="mail")
	private List<Attachment> attachmentList;

}
