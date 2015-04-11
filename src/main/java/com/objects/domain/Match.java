package com.objects.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Match {
	@JsonProperty("user")
	private User user;
	@JsonProperty("offerable")
	private TradingEntity offerable;
	@JsonProperty("desirable")
	private TradingEntity desirable;
 
	public Match() {
	}
	
	public Match(User user, TradingEntity offerable, TradingEntity desirable) {
		this.user = user;
		this.offerable = offerable;
		this.desirable = desirable;
	}
 
	public void setUser(User user) {
		this.user = user;
	}

	public void setOfferable(TradingEntity offerable) {
		this.offerable = offerable;
	}

	public void setDesirable(TradingEntity desirable) {
		this.desirable = desirable;
	}

	@Override
	public String toString() {
		return "Match [user=" + user + ", offerable=" + offerable + ", desirable=" + desirable + "]";
	}
}
