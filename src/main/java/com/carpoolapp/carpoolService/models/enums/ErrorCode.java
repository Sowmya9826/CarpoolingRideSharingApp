package com.carpoolapp.carpoolService.models.enums;

public enum ErrorCode {
    UNSPECIFIED("Sorry, Something Went Wrong!"),
    NO_RIDE_AVAILABLE("Sorry, Right now there are not riders available!");

    private String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
