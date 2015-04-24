package com.objects.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Match {
	@JsonProperty("id")
	private long id;
	@JsonProperty("offerer")
	private User offerer;
	@JsonProperty("offerable")
	private TradingEntity offerable;
	@JsonProperty("desirable")
	private TradingEntity desirable;
 
	public Match() {
	}
	
	public Match(long id, User offerer, TradingEntity offerable, TradingEntity desirable) {
		this.id = id;
		this.offerer = offerer;
		this.offerable = offerable;
		this.desirable = desirable;
	}
 
	public User getOfferer() {
		return offerer;
	}

	public void setOfferer(User offerer) {
		this.offerer = offerer;
	}

	public void setOfferable(TradingEntity offerable) {
		this.offerable = offerable;
	}

	public void setDesirable(TradingEntity desirable) {
		this.desirable = desirable;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + "offerer=" + offerer + ", offerable=" + offerable + ", desirable=" + desirable + "]";
	}
}
