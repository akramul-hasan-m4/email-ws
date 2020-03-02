package mail.atililimted.net.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mail.atililimted.net.entity.Mail;

@Repository
public interface MailRepo extends JpaRepository<Mail, BigInteger>{

}
