package com.qzct.immediatechoice.domain;

public class conversation {

	public String content;
	public String addresser;
	public String addressee;
	public String portraiturl;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPortraiturl() {
		return portraiturl;
	}
	public void setPortraiturl(String portraiturl) {
		this.portraiturl = portraiturl;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getAddresser() {
		return addresser;
	}
	public void setAddresser(String addresser) {
		this.addresser = addresser;
	}
	public conversation(String content, String portraiturl, String addressee,
			String addresser) {
		super();
		this.content = content;
		this.portraiturl = portraiturl;
		this.addressee = addressee;
		this.addresser = addresser;
	}
	@Override
	public String toString() {
		return "conversation [content=" + content + ", portraiturl="
				+ portraiturl + ", addressee=" + addressee + ", addresser="
				+ addresser + "]";
	}
	
}
