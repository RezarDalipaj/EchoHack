package de.dlh.lhind.ecohack.exception.custom;

import de.dlh.lhind.ecohack.util.constants.Constants;

public class UnAuthorizedException extends GlobalException{
    public UnAuthorizedException(String message) {
        super(message);
    }
    public UnAuthorizedException(){
        super(Constants.UNAUTHORIZED_MESSAGE);
    }
}
