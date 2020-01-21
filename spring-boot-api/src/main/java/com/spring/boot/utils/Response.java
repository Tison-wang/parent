package com.spring.boot.utils;

public class Response<T> {

	private final static String SUCCESS_MSG = "success";

	private final static String FAILED_MSG = "failed";

	private Boolean result;

	private String msg;

	private Integer code;

	private T content;
	
	public static Response ok(){
		return Response.of(true, SUCCESS_MSG,200, null);
	}
	
	public static <T> Response ok(T content){
		return Response.of(true, SUCCESS_MSG,200, content);
	}

	public static <T> Response ok(Boolean result, String msg, Integer code, T content){
		return Response.of(result, msg, code, content);
	}

	public static Response failure(Integer code){
		return Response.of(false, FAILED_MSG, code, null);
	}

	public static <T> Response failure(Boolean result, String msg, Integer code, T content){
		return Response.of(result, msg, code, content);
	}

	public static <T> Response of(Boolean result,  String msg, Integer code, T re){
		return new Response(result,msg, code, re);
	}
	
	public Response(Boolean result, String msg, Integer code, T re){
		this.result = result;
		this.msg = msg;
		this.code = code;
		this.content = re;
	}
	
	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
