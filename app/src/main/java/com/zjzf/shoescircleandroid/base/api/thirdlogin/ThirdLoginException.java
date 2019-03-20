package com.zjzf.shoescircleandroid.base.api.thirdlogin;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public class ThirdLoginException extends RuntimeException {
    public ThirdLoginException() {
    }

    public ThirdLoginException(String message) {
        super(message);
    }

    public ThirdLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThirdLoginException(Throwable cause) {
        super(cause);
    }

}
