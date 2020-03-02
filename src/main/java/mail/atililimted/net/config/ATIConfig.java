package mail.atililimted.net.config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ATIConfig {

	@Value("${mail.store.protocol}")
	private String protocol;
	
	@Value("${ati.mail.store.type}")
	private String host;
	
	@Value("${mail.pop3.port}")
	private String port;
	
	@Value("${mail.pop3.starttls.enable}")
	private String enable;
	
	@Value("${mail.smtp.auth}")
	private String smtpAuth;
	
	@Value("${mail.smtp.starttls.enable}")
	private String smtpStarttls;
	
	@Value("${mail.smtp.port}")
	private String smtpPort;
	
	@Bean("properties")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Properties properties() {
		 Properties properties = new Properties();
		
		properties.put("mail.store.protocol", protocol);
		properties.put("mail.pop3s.host", host);
		properties.put("mail.pop3s.port", port);
		properties.put("mail.pop3.starttls.enable", enable);
		 
         
		/*
		 * properties.put("mail.smtp.auth", smtpAuth);
		 * properties.put("mail.smtp.starttls.enable", smtpStarttls);
		 * properties.put("mail.smtp.host", host); properties.put("mail.smtp.port",
		 * "25");
		 */
         
		 properties.put("mail.smtp.auth", "true");
		 properties.put("mail.smtp.starttls.enable", "true");
		 properties.put("mail.smtp.host", "mail.atilimited.net");
		 properties.put("mail.smtp.port", "25");
         
         return properties;
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public String uploadPath() throws UnsupportedEncodingException {
		String splitter = File.separator.replace("\\", "\\\\");
		String mainPath = splitter + "static" + splitter + "Attachment" + splitter;
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String pathArr[] = fullPath.split(splitter + "target" + splitter + "classes" + splitter);
		String filePath = new File(pathArr[0]).getPath() + mainPath;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return new File(pathArr[0]).getPath() + mainPath;
	}
	
}
