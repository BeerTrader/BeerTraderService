package com.objects.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Match {
	@JsonProperty("id")
	private long id;
	@JsonProperty("offerer")
	private User offerer;
	@JsonProperty("desirer")
	private User desirer;
	@JsonProperty("offerable")
	private TradingEntity offerable;
	@JsonProperty("desirable")
	private TradingEntity desirable;
 
	public Match() {
	}
	
	public Match(long id, User offerer, User desirer, TradingEntity offerable, TradingEntity desirable) {
		this.id = id;
		this.offerer = offerer;
		this.desirer = desirer;
		this.offerable = offerable;
		this.desirable = desirable;
	}
 
	public long getId() {
		return id;
	}

	public User getOfferer() {
		return offerer;
	}

	public User getDesirer() {
		return desirer;
	}
	
	public TradingEntity getOfferable() {
		return offerable;
	}
	
	public TradingEntity getDesirable() {
		return desirable;
	}	

	@Override
	public String toString() {
		return "Match [id=" + id + "offerer=" + offerer + ", desirer=" + desirer + ", offerable=" + offerable + ", desirable=" + desirable + "]";
	}
}
