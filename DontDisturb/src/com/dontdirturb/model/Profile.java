package com.dontdirturb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Profile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long profileId;
	private String profileName;
	private String initSchedule;
	private String endSchedule;
	private Integer blockCalls = 0;
	private Integer blockSMS   = 0;
	private Date registryDate;
	private List<Contact> persons;
	
	public List<Contact> getPersons() {
		return persons;
	}
	public void setPersons(List<Contact> persons) {
		this.persons = persons;
	}
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getInitSchedule() {
		return initSchedule;
	}
	public void setInitSchedule(String initSchedule) {
		this.initSchedule = initSchedule;
	}
	public String getEndSchedule() {
		return endSchedule;
	}
	public void setEndSchedule(String endSchedule) {
		this.endSchedule = endSchedule;
	}
	public Integer getBlockCalls() {
		return blockCalls;
	}
	public void setBlockCalls(Integer blockCalls) {
		this.blockCalls = blockCalls;
	}
	public Integer getBlockSMS() {
		return blockSMS;
	}
	public void setBlockSMS(Integer blockSMS) {
		this.blockSMS = blockSMS;
	}

	public Date getRegistryDate() {
		return registryDate;
	}
	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}	

}
