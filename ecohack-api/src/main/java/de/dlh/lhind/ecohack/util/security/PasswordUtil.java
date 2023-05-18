package de.dlh.lhind.ecohack.util.security;

import de.dlh.lhind.ecohack.util.constants.Constants;
import lombok.experimental.UtilityClass;
@UtilityClass
public final class PasswordUtil {

    private final String salt = Constants.SALT;
    private final Integer halfSaltLength = salt.length()/2;

    public static String getSaltedPassword(String password){
        var halfSalt = salt.substring(0, halfSaltLength);
        return halfSalt + password.concat(salt);
    }
}
