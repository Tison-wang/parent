package com.base.common.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -4658131138766127845L;

    public final static int SUCCESS_STATUS = 0;

    public final static int FAILED_STATUS = -1;

    private Integer status;

    private Boolean result;

    private String msg;

    private Integer code;

    private T content;

    public static Response ok() {
        return Response.of(true, null, 200, null);
    }

    public static <T> Response ok(T content) {
        return Response.of(true, null, 200, content);
    }

    public static <T> Response ok(Boolean result, String msg, Integer code, T content) {
        return Response.of(result, msg, code, content);
    }

    public static Response failure(String msg, Integer code) {
        return Response.of(false, msg, code, null);
    }

    public static <T> Response failure(Boolean result, String msg, Integer code, T content) {
        return Response.of(result, msg, code, content);
    }

    public static <T> Response of(Boolean result, String msg, Integer code, T re) {
        if (result) {
            return new Response(SUCCESS_STATUS, result, msg, code, re);
        } else {
            return new Response(FAILED_STATUS, result, msg, code, re);
        }
    }

    public Response(Integer status, Boolean result, String msg, Integer code, T re) {
        this.status = status;
        this.result = result;
        this.msg = msg;
        this.code = code;
        this.content = re;
    }

    public String toString() {
        StringBuilder sb = (new StringBuilder("{")).append("\"status\":").append(this.status).append(", \"result\":").append(this.result).append(", \"msg\":\"").append(this.msg).append("\", \"code\":").append(this.code).append(", \"content\":").append(this.content).append("}");
        return sb.toString();
    }

}
