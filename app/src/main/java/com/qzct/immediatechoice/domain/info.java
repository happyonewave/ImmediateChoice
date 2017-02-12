package com.qzct.immediatechoice.domain;

public class info {

	private String student;
	private String date;
	private String imageurl;
	public String getStudent() {
		return student;
	}
	public void setStudnet(String studnet) {
		this.student = studnet;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	@Override
	public String toString() {
		return "info [studnet=" + student + ", date=" + date + ", imageurl="
				+ imageurl + "]";
	}
	public info(String student, String date, String imageurl) {
		super();
		this.student = student;
		this.date = date;
		this.imageurl = imageurl;
	}
	public info() {
		super();
	}
	
}
