package ru.omnicomm.santa.entity;

public class MailInfo {

    private String santaMail;
    private User user;

    public String getSantaMail() {
        return santaMail;
    }

    public void setSantaMail(String santaMail) {
        this.santaMail = santaMail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
