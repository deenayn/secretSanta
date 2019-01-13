package ru.omnicomm.santa.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.omnicomm.santa.ServerProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Utils {

    @Autowired
    ServerProperty serverProperty;

    public boolean canAddNewSanta() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date toDate = dateFormat.parse(serverProperty.getToDate());
        long toTimestamp = toDate.getTime();
        Date nowDate = new Date();
        long nowTimestamp = nowDate.getTime();
        return nowTimestamp < toTimestamp;
    }

    public String createConfirmLink(String lastnameHash) {
        return serverProperty.getAddress() + "/emailConfirm?hs=" + lastnameHash;
    }
}
