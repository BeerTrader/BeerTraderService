package com.objects.domain;

import org.neo4j.graphdb.Node;

public class MatchNode {
	private Node offerer;
	private Node desirer;
	private Node offerable;
	private Node desirable;
	
	public MatchNode(Node offerer, Node desirer, Node offerable, Node desirable) {
		this.offerer = offerer;
		this.offerer = offerable;
		this.offerer = desirable;
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