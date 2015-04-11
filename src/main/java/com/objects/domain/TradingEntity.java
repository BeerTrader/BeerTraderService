package com.objects.domain;

import java.util.List;

import org.neo4j.graphdb.Node;

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
	
	public static TradingEntity convertToTradingEntity(Node te) {
		return new TradingEntity(te.getId(), te.getLabels().iterator().next().name(), te.getProperty("name").toString(), null);
	}
 
	@Override
	public String toString() {
		return "TradingEntity [id=" + id + ", label=" + label + ", name=" + name + ", relations=" + relations + "]";
	}
}
