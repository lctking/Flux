package com.lctking.fluxthrottle.constant;

public enum ThrottleResultEnum {
    SUCCESS("success"),
    FAIL("fail");

    public final String value;

    ThrottleResultEnum(String value){
        this.value = value;
    }
}
