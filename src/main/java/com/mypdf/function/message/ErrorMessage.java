package com.mypdf.function.message;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public enum ErrorMessage {

    EMPTY_INPUT("Operation failed. Please, provide non-empty arrays of full file names."),
    INVALID_OUTPUT("Operation failed. Invalid output files number."),
    INPUT_OUTPUT_SIZE_ERROR("Operation failed. Array of input and output files must be equal size.");


    private String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    @Override
    public String toString() {
        return errorMessage;
    }

}
