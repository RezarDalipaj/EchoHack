package de.dlh.lhind.ecohack.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public final class PasswordUtil {

    @Value("${app.salt}")
    private static String salt;

    public static String getSaltedPassword(String password){
        var halfSalt = salt.substring(0, salt.length()/2);
        return halfSalt + password.concat(salt);
    }
}
