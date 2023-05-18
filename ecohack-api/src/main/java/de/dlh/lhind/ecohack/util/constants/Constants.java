package de.dlh.lhind.ecohack.util.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {

    public static final String NOT_FOUND_MESSAGE = "Not found";
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized!";
    public static final String REFRESH_PATH = "/refresh/token";
    public static final String SALT = "fouwrhvn";
    public static final String ERROR_MESSAGE = "An error occurred";
    public static final String LOGOUT_SUCCESS = "You have successfully logged out";

    @UtilityClass
    public static final class Token {
        public static final String TOKEN_TYPE = "JWT";
        public static final String TOKEN_ISSUER = "order-api";
        public static final String TOKEN_AUDIENCE = "order-app";
        public static final String TOKEN_HEADER = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
    }
}
