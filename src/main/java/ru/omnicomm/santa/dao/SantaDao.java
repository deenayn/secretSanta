package ru.omnicomm.santa.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.omnicomm.santa.entity.User;
import ru.omnicomm.santa.entity.MailInfo;

import java.util.List;

@Component
public class SantaDao {

    private final JdbcTemplate jdbcTemplate;

    public SantaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addSanta(String mail, String lastName, String hash) {
        String sql = "INSERT INTO users VALUES ('" + mail + "', '" + lastName + "', '" + hash + "') ON CONFLICT (mail) DO NOTHING;";
        jdbcTemplate.execute(sql);
    }

    public void acceptSanta(String hash) {
        String sql = "UPDATE users SET accepted = true WHERE hash = '" + hash + "';";
        jdbcTemplate.update(sql);
    }

    public List<User> getAllAcceptedSantas() {
        String sql = "SELECT mail, lastName, wishlist FROM users WHERE accepted IS TRUE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setEmail(rs.getString("mail"));
            user.setName(rs.getString("lastName"));
            user.setWishlist(rs.getObject("wishlist", String.class));
            return user;
        });
    }

    public void addUserSanta(String userMail, String santaMail) {
        String sql = "INSERT INTO usersSanta VALUES ('" + userMail + "', '" + santaMail + "') ON CONFLICT (userMail) DO NOTHING;";
        jdbcTemplate.execute(sql);
    }

    public List<MailInfo> getInfoForMail() {
        String sql = "SELECT userMail, lastName, wishlist, santaMail from usersSanta LEFT JOIN users on users.mail = userMail;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MailInfo mailInfo = new MailInfo();
            User user = new User();
            user.setName(rs.getString("lastName"));
            user.setEmail(rs.getString("userMail"));
            user.setWishlist(rs.getObject("wishlist", String.class));
            mailInfo.setUser(user);
            mailInfo.setSantaMail(rs.getString("santamail"));
            return mailInfo;
        });
    }
}
