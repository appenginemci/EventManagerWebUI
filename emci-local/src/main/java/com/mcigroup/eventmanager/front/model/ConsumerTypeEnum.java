package com.mcigroup.eventmanager.front.model;

public enum ConsumerTypeEnum {
	MANAGER("manager"),
	USER("user");
	
	private final String consumerType;
	private ConsumerTypeEnum(String consumerType) {
		this.consumerType = consumerType;
	}
	public String getConsumerType() {
		return consumerType;
	}
}
