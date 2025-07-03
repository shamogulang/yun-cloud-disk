package cn.yun.oddworld.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    int code;

    String message;

    T data;

    public BaseResult() {}

    public BaseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult<T> success(T data){
         this.setData(data);
         return this;
    }
}
