package com.rent.util;

import java.io.Serializable;

public class Result implements Serializable{ //Serializable网络传输时需要的序列化接口
	private String status;
	private String message;
	public Object data;
	
	public Result(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public Result(String status, String message) {
		this.status = status;
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
