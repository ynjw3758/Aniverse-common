package com.Aniverse.Common.dto;

import lombok.Data;

@Data
public class Response {
	private int resultcode;
	private String resultMsg;
	private Object resultData;
	private String ver_code;
	private String certifi_number;
	private String fromnumber;
	
	
	public void setFromnumber(String fromnumber) {
		this.fromnumber = fromnumber;
		
	}
	public void setVercode(String ver_code) {
		this.ver_code = ver_code;
	}
	public void setCertifi_number(String certifi_number) {
		this.certifi_number = certifi_number;
	}
	
	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
	public String getFromnumber() {
		return fromnumber;
	}
	public String getCertifi_number() {
		return certifi_number;
	}
	public String getVercode() {
		return ver_code;
	}
	
	public int getResultcode() {
		return resultcode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public Object getResultData() {
		return resultData;
	}
	
	@Override
	public String toString() {
		return "{" + "resultcode : " + resultcode+ " , " + "resultMsg : " + resultMsg +" , " + "resultData : " + resultData +"}";
	}
}
