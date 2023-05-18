package de.dlh.lhind.ecohack.exception.custom;

import de.dlh.lhind.ecohack.util.constants.Constants;

public class GlobalException extends Exception{
    public GlobalException(String message){
        super(message);
    }

    public GlobalException(){
        super(Constants.ERROR_MESSAGE);
    }
}
