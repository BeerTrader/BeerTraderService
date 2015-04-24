package com.objects.domain;

import org.neo4j.graphdb.Node;

public class MatchNode {
	private long id;
	private Node offerer;
	private Node desirer;
	private Node offerable;
	private Node desirable;
	
	public MatchNode(Node offerer, Node desirer, Node offerable, Node desirable) {
		this.offerer = offerer;
		this.desirer = desirer;
		this.offerable = offerable;
		this.desirable = desirable;
	}

	public MatchNode(long id, Node offerer, Node desirer, Node offerable, Node desirable) {
		this.id = id;
		this.offerer = offerer;
		this.desirer = desirer;
		this.offerable = offerable;
		this.desirable = desirable;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Node getOfferer() {
		return offerer;
	}

	public void setOfferer(Node offerer) {
		this.offerer = offerer;
	}

	public Node getDesirer() {
		return desirer;
	}

	public void setDesirer(Node desirer) {
		this.desirer = desirer;
	}

	public Node getOfferable() {
		return offerable;
	}

	public void setOfferable(Node offerable) {
		this.offerable = offerable;
	}

	public Node getDesirable() {
		return desirable;
	}

	public void setDesirable(Node desirable) {
		this.desirable = desirable;
	}
}