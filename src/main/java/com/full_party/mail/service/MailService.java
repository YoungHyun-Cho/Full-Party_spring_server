package com.full_party.mail.service;

import com.full_party.mail.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String SENDER_EMAIL = "fullpartyspring@gmail.com";
    private static Integer number;

    private static void createNumber() {
        number = (int)(Math.random() * 90000) + 100000;
    }

    private static String composeContent() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "      <html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "        <head>\n" +
                "          <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "          <title>FullParty Verification Email</title>\n" +
                "          <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                "        </head>\n" +
                "        <body style=\"margin: 0; padding: 0;\">\n" +
                "          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "            <tr>\n" +
                "              <td align=\"center\"style=\"padding: 40px 0 10px 0;\">\n" +
                "                <img src=\"https://fullpartyspringimageserver.s3.ap-northeast-2.amazonaws.com/defaultProfile.png\" alt=\"fullParty Thumbnail\" width=\"500\" height=\"500\" style=\"display: block;\" />\n" +
                "             </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td align=\"center\">\n" +
                "                풀팟 회원 가입을 위한 인증 코드입니다.\n" +
                "                <br />아래의 인증 코드를 입력하여 이메일 인증을 완료해 주세요.\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td align=\"center\" style=\"padding: 20px 0 20px 0;\">\n" +
                "                <font size=\"5pt\" color=\"#50C9C3\"><b>" + number + "</b></font>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </body>\n" +
                "      </html>";
    }

    private MimeMessage createMail(String email) {

        createNumber();

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(SENDER_EMAIL);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[풀팟] 이메일 인증을 진행해 주세요.");
            message.setText(composeContent(), "UTF-8", "html");
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String email) {

        MimeMessage message = createMail(email);

        javaMailSender.send(message);

        return number;
    }
}
