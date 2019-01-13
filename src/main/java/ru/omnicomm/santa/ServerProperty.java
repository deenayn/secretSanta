package ru.omnicomm.santa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerProperty {

    @Value("${date.work.toDate}")
    private String toDate;

    @Value("${smtp.server.ip}")
    private String smtp;

    @Value("${smtp.from}")
    private String email;

    @Value("${address}")
    private String address;


    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
