package entity;

import java.io.Serializable;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/4/16 20:54
 * @see
 */
public class Result implements Serializable {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回信息
     */
    private String message;

    public Result(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
