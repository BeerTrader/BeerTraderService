package com.objects.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TradingEntity {
	private long id;
	@JsonProperty("label")
	private String label;
	@JsonProperty("name")
	private String name;
	@JsonProperty("relations")
	private List<TradingEntity> relations;
 
	public TradingEntity() {
	}
	
	public TradingEntity(long id, String label, String name, List<TradingEntity> relations) {
		this.id = id;
		this.label = label;
		this.name = name;
		this.relations = relations;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<TradingEntity> getRelations() {
		return this.relations;
	}
 
	@Override
	public String toString() {
		return "TradingEntity [id=" + id + ", label=" + label + ", name=" + name + ", relations=" + relations + "]";
	}
}
