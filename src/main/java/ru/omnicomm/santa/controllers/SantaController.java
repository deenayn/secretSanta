package ru.omnicomm.santa.controllers;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.omnicomm.santa.dao.SantaDao;
import ru.omnicomm.santa.entity.User;
import ru.omnicomm.santa.entity.MailInfo;
import ru.omnicomm.santa.services.SmtpServer;
import ru.omnicomm.santa.utils.Utils;

import java.util.Collections;
import java.util.List;

@Controller()
public class SantaController {

    Logger logger = LoggerFactory.getLogger(SantaController.class);

    @Autowired
    private SantaDao santaDao;

    @Autowired
    private Utils utils;

    @Autowired
    private SmtpServer smtpServer;

    @PostMapping(value = "addSanta")
    public ResponseEntity addSanta(@RequestBody User newUser) {
        try {
            if (!utils.canAddNewSanta()) {
                return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
            }
            String lowerEmail = newUser.getEmail().toLowerCase();
            String hash = DigestUtils.sha256Hex(lowerEmail);
            santaDao.addSanta(lowerEmail, newUser.getName(), hash);
            Thread t = new Thread(() -> {
                try {
                    smtpServer.sendMessage(newUser.getEmail(), "Тайный санта ОРПО", "Привет! " +
                            "\nТы зарегистрировался для участия в Тайном Санте ОРПО. " +
                            "Для подтверждения участия пройди по ссылке: " + utils.createConfirmLink(hash) +
                            "\nЕсли ты не оставлял заявку, значит это сделал какой-то плохой человек вместо тебя. Просто игнорируй это сообщение." +
                            "\nХорошего дня!");
                } catch (Exception e) {
                    logger.error("Something wrong with sendMessage! Error {}", e);
                }
            });
            t.start();
            logger.info("Added new santa lastName {}, email {}", newUser.getName(), newUser.getEmail());
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Something wrong with addSanta! Error {}", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/")
    public String welcome() {
        return "index";
    }

    @GetMapping(value = "/emailConfirm")
    public String emailConfirm(@RequestParam String hs) {
        try {
            santaDao.acceptSanta(hs);
            return "confirm";
        } catch (Exception e) {
            logger.error("Something wrong with emailConfirm! Error {}", e);
            return "confirm-fail";
        }

    }

    @GetMapping(value = "/startChoosingSanta")
    public ResponseEntity startChoosingSanta() {
        try {
            List<User> users = santaDao.getAllAcceptedSantas();
            Collections.shuffle(users);
            User prevUser = null;
            for (User user : users) {
                if (prevUser == null) {
                    prevUser = users.get(users.size() - 1);
                }
                santaDao.addUserSanta(user.getEmail(), prevUser.getEmail());
                prevUser = user;
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Something wrong with startChoosingSanta! Error {}", e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/sendMessagesToSanta")
    public ResponseEntity sendMessagesToSanta() {
        try {
            List<MailInfo> mailInfoList = santaDao.getInfoForMail();
            for (MailInfo mi : mailInfoList) {
                Thread t = new Thread(() -> {
                    String message = "Привет! " +
                            "\nМы провели распределение. " +
                            "Тебе достался следующий участник: " + mi.getUser().getName() + ". Почта " + mi.getUser().getEmail();
                    if (mi.getUser().getWishlist() != null) {
                        message += "\nНебольшие пожелания от участника по поводу подарка: " + mi.getUser().getWishlist();
                    }
                    message += "\nТеперь осталось только придумать и приобрести классный подарок, а потом отнести его к Игровую :)" +
                            " Там мы специально оставили мешок Санты для этого. Не забудь подписать подарок!" +
                            "\n\nЕсли тебе пришло два подобных письма, сообщи нам об этом, пожалуйста" +
                            "\nХорошего дня и удачи!";
                    try {
                        smtpServer.sendMessage(mi.getSantaMail(), "Тайный санта ОРПО", message);
                    } catch (Exception e) {
                        logger.error("Something wrong with sendMessage! Error {}", e);
                    }
                });
                t.start();
            }
        } catch (Exception e) {
            logger.error("Something wrong with sendMessagesToSanta! Error {}", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
