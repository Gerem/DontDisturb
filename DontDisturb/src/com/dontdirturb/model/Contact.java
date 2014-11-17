package com.dontdirturb.model;

import java.io.Serializable;
import java.util.Date;

import com.dontdisturb.types.CallType;

import android.graphics.Bitmap;

public class Contact implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long contactId;
	private String name;
	private String phone;
	private String imageUri;
	private Long profileFk;
	private Date registryDate;
	private Integer contactType;
	private Bitmap photo;
	private boolean selected = false;
	private CallType callType;
	private String callDuration;
	private String callTime;
	private String msg;
	private Profile profile;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public Long getProfileFk() {
		return profileFk;
	}
	public void setProfileFk(Long profileFk) {
		this.profileFk = profileFk;
	}
	public Date getRegistryDate() {
		return registryDate;
	}
	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}
	public Integer getContactType() {
		return contactType;
	}
	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public CallType getCallType() {
		return callType;
	}
	public void setCallType(CallType callType) {
		this.callType = callType;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getMsg() {
		return msg;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
