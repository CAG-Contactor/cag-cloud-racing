package se.caglabs.cloudracing.common.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.sql.Timestamp;

@DynamoDBTable(tableName="current-race")
public class CurrentRace {
	public enum Status {
		IDLE, ARMED, STARTED, FINISHED, ABORTED
	}
	private String id;
	private Timestamp startTime;
	private Timestamp splitTime;
	private Timestamp finishTime;
	private Status status;

	public CurrentRace(String id, Timestamp startTime, Timestamp splitTime, Timestamp finishTime, Status status) {
		this.id = id;
		this.startTime = startTime;
		this.splitTime = splitTime;
		this.finishTime = finishTime;
		this.status = status;
	}

	@DynamoDBHashKey(attributeName="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName="status")
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@DynamoDBAttribute(attributeName="startTime")
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@DynamoDBAttribute(attributeName="splitTime")
	public Timestamp getSplitTime() {
		return this.splitTime;
	}

	public void setSplitTime(Timestamp splitTime) {
		this.splitTime = splitTime;
	}

	@DynamoDBAttribute(attributeName="finishTime")
	public Timestamp getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
}
