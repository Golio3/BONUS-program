package bonus.web.controller;

import bonus.web.model.BonusModel;
import bonus.web.model.UserBonusModel;
import bonus.web.model.UsersModel;
import bonus.web.repository.BonusRepository;
import bonus.web.repository.UserBonusRepository;
import bonus.web.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/manage")
public class UserController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BonusRepository bonusRepository;

    @Autowired
    UserBonusRepository userBonusRepository;

    private Integer modal = 0;

    @GetMapping("/user")
    public String userForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        UsersModel user = usersRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<BonusModel> bonuses = bonusRepository.findAll();
        bonuses.forEach(folder -> {
            if (folder.getImage() != null) {
                String img = Base64.getEncoder().encodeToString(folder.getImage());
                folder.setImageStr(img);
            }
        });
        model.addAttribute("bonuses", bonuses);

        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        Integer costAll = 0, numberAll = 0;
        for (UserBonusModel ub : userBonus) {
            costAll = costAll + ub.getBonus().getCost() * ub.getNumber();
            numberAll = numberAll + ub.getNumber();
        }

        model.addAttribute("numberAll", numberAll);
        model.addAttribute("costAll", costAll);

        model.addAttribute("modal", modal);
        if (modal == 1) {
            modal = 0;
        }

        return "admin/manage/user/user";
    }

    @GetMapping("/user/{id}/add/{bonusId}")
    public String addBonus(@PathVariable Integer id, @PathVariable Integer bonusId) {
        UsersModel user = usersRepository.findOne(id);
        BonusModel bonus = bonusRepository.findOne(bonusId);

        UserBonusModel userBonus = userBonusRepository.findByUserIdAndBonusIdAndStatus(id, bonusId, 1);
        if (userBonus == null) {
            userBonus = new UserBonusModel(1, 1, user, bonus);
        } else {
            userBonus.setNumber(userBonus.getNumber() + 1);
        }

        userBonusRepository.save(userBonus);
        user.setPointsActual(user.getPointsActual() - bonus.getCost());
        usersRepository.save(user);
        return "redirect:/manage/user";
    }

    @GetMapping("/user/bonus")
    public String listBonus(Model model, Authentication authentication) {
        String username = authentication.getName();
        UsersModel user = usersRepository.findByUsername(username);
        model.addAttribute("user", user);

        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        Integer costAll = 0;
        for (UserBonusModel ub : userBonus) {
            costAll = costAll + ub.getBonus().getCost() * ub.getNumber();
        }
        model.addAttribute("userBonus", userBonus);
        model.addAttribute("costAll", costAll);

        return "admin/manage/user/order";
    }

    @GetMapping("/user/bonus/minus/{id}")
    public String minusBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        user.setPointsActual(user.getPointsActual() + userBonus.getBonus().getCost());
        if (userBonus.getNumber() > 1) {
            userBonus.setNumber(userBonus.getNumber() - 1);
            userBonusRepository.save(userBonus);
        } else {
            userBonusRepository.delete(userBonus);
        }
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/plus/{id}")
    public String plusBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        if (user.getPointsActual() >= userBonus.getBonus().getCost()) {
            user.setPointsActual(user.getPointsActual() - userBonus.getBonus().getCost());
            usersRepository.save(user);
            userBonus.setNumber(userBonus.getNumber() + 1);
            userBonusRepository.save(userBonus);
        }
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/remove/{id}")
    public String removeBonus(@PathVariable Integer id) {
        UserBonusModel userBonus = userBonusRepository.findOne(id);
        UsersModel user = userBonus.getUser();
        user.setPointsActual(user.getPointsActual() + userBonus.getBonus().getCost());
        userBonusRepository.delete(userBonus);
        return "redirect:/manage/user/bonus";
    }

    @GetMapping("/user/bonus/confirm")
    public String confirmBonus(Authentication authentication) throws IOException, MessagingException {
        String username = authentication.getName();
        UsersTESTModel user = usersRepository.findByUsername(username);
        String context = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n" +
                "</head>\n" +
                "<body>";
        context += "Vam přišla objednavka na bonus produkty od uživatele " + user.getUsername() + ":<br>";
        context += emailTableHeader();
        List<UserBonusModel> userBonus = userBonusRepository.findAllByUserIdAndStatus(user.getId(), 1);
        Integer costAll = 0;
        for (UserBonusModel ub : userBonus) {
            UserBonusModel oldUb = userBonusRepository.findByUserIdAndBonusIdAndStatus(ub.getUser().getId(), ub.getBonus().getId(), 2);
            if (oldUb != null) {
                oldUb.setNumber(oldUb.getNumber() + ub.getNumber());
                userBonusRepository.save(oldUb);
                userBonusRepository.delete(ub);
            } else {
                ub.setStatus(2);
                userBonusRepository.save(ub);
            }
            costAll = costAll + ub.getBonus().getCost() * ub.getNumber();
            context += emailTable(ub.getBonus().getName(), ub.getBonus().getCost(), ub.getNumber(), ub.getSum());
        }
        context += emailTableEnd();
        context += "Celkem: " + costAll + "<br>";
        context += "Addresa: " + user.getAddress() + " " + user.getCity() + " " + user.getPcs();
        context += "</body>\n </html>";
        user.setPoints(user.getPointsActual());
        usersRepository.save(user);

        sendmail("pavel.golio3@gmail.com", "Objednavka na bonus produkty", context);
        sendmail(user.getEmail(), "Objednavka na bonus produkty", context);
        modal = 1;

        return "redirect:/manage/user";
    }

    private void sendmail(String email, String title, String context) throws MessagingException, IOException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        //Set gmail email id
        mailSender.setUsername("pavel.golio3@gmail.com");
        //Set gmail email password
        mailSender.setPassword("112gendalf");
        Properties prop = mailSender.getJavaMailProperties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.debug", "true");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
        mailMsg.setFrom("pavel.golio3@gmail.com");
        mailMsg.setTo(email);
        mailMsg.setSubject(title);
        mailMsg.setText(context, true);
        mailSender.send(mimeMessage);

//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("pavel.golio3@gmail.com", "112gendalf");
//            }
//        });
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress("pavel.golio3@gmail.com", false));
//
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//        msg.setSubject(title);
//        msg.setContent(context, "text/html");
//        msg.setSentDate(new Date());
//
//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent(context, "text/html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
////        MimeBodyPart attachPart = new MimeBodyPart();
////        attachPart.attachFile("/var/tmp/image19.png");
////        multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);
//        Transport.send(msg);
    }

    private String emailTableHeader() {
        String result = "<table style=\"width: 100%; max-width: 100%; margin-bottom: 20px; border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse;\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td style=\"border: 1px solid #ddd; padding: 8px; line-height: 1.42857143; vertical-align: top;\">\n" +
                "                                    Dárek\n" +
                "                                </td>\n" +
                "                                <td style=\"border: 1px solid #ddd; padding: 8px; line-height: 1.42857143; vertical-align: top;\">\n" +
                "                                    Body\n" +
                "                                </td>\n" +
                "                                <td style=\"border: 1px solid #ddd; padding: 8px; line-height: 1.42857143; vertical-align: top;\">\n" +
                "                                    Počet\n" +
                "                                </td>\n" +
                "                                <td style=\"border: 1px solid #ddd; padding: 8px; line-height: 1.42857143; vertical-align: top;\">\n" +
                "                                    Součet\n" +
                "                                </td>\n" +
                "                            </tr>\n";
        return result;
    }

    private String emailTable(String name, Integer cost, Integer number, Integer sum) {
        String result = "<tr>\n" +
                "                                <td>\n" +
                "                                    <div style=\"padding: 15px; border: 1px solid transparent; border-radius: 4px; color: #31708f; background-color: #d9edf7; border-color: #bce8f1; margin: 0;\">\n" +
                name +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                    <div style=\"padding: 15px; border: 1px solid transparent; border-radius: 4px; color: #31708f; background-color: #d9edf7; border-color: #bce8f1; margin: 0;\">\n" +
                cost +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                    <div style=\"padding: 15px; border: 1px solid transparent; border-radius: 4px; color: #31708f; background-color: #d9edf7; border-color: #bce8f1; margin: 0;\">\n" +
                number +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                    <div style=\"padding: 15px; border: 1px solid transparent; border-radius: 4px; color: #31708f; background-color: #d9edf7; border-color: #bce8f1; margin: 0;\">\n" +
                sum +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                            </tr>";
        return result;
    }

    private String emailTableEnd() {
        String result = "</tbody>\n" +
                "                        </table>\n";
        return result;
    }
}
