package com.ak6l.MorseTrainer;

public class MorseElement {
	private String element;
	private String code;
	private Integer ditCount;
	
	public MorseElement(String element, String code, Integer ditCount) {
		this.element = element;
		this.code = code;
		this.ditCount = ditCount;
	}
	
	public String getElement() {
		return this.element;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public Integer getDitCount() {
		return this.ditCount;
	}
}
