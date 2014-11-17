package com.dontdirturb.model;

import java.util.Date;

public class RegistryBlock {
	private Long idRegistryBlockPk;	
	private Integer blockType;
	private String contactName;
	private String contactPhone;
	private String imageUri;
	private Date registryDate;
	private String callTime;	
	private String msg;
	private String profile;
	
	public Long getIdRegistryBlockPk() {
		return idRegistryBlockPk;
	}
	public void setIdRegistryBlockPk(Long idRegistryBlockPk) {
		this.idRegistryBlockPk = idRegistryBlockPk;
	}
	public Integer getBlockType() {
		return blockType;
	}
	public void setBlockType(Integer blockType) {
		this.blockType = blockType;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public Date getRegistryDate() {
		return registryDate;
	}
	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

}
