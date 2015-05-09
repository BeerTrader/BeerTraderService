package com.objects.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.exceptions.ObjectMappingException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objects.mapping.ObjectManager;

import ibt.ortc.api.ChannelPermissions;
import ibt.ortc.api.InvalidBalancerServerException;
import ibt.ortc.api.Ortc;
import ibt.ortc.api.OrtcAuthenticationNotAuthorizedException;

public class SimpleMessaging {
	private static String applicationKey = "NZHf5g";
	private static String privateKey = "LHLE4LTvdQe3";
	private static String url = "https://ortc-developers.realtime.co/server/ssl/2.1";
	private static boolean isCluster = true;
	private static boolean authenticationTokenIsPrivate = true;
	private static int timeToLive = 1800; //30 minutes
	
	@JsonProperty("channelName")
	private String channelName;
	@JsonProperty("authenticationToken")
	private String authenticationToken;
	@JsonProperty("authSaved")
	private boolean authSaved;
	
	public SimpleMessaging(String channelName, String authenticationToken) {
		this.channelName = channelName;
		this.authenticationToken = authenticationToken;
		this.authSaved = false;
	}
	
	public void realtimeAuthenticate() throws IOException, InvalidBalancerServerException, OrtcAuthenticationNotAuthorizedException {
		HashMap<String, LinkedList<ChannelPermissions>> permissions = new HashMap<String, LinkedList<ChannelPermissions>>();
		 
		LinkedList<ChannelPermissions> channelPermissions = new LinkedList<ChannelPermissions>();
		channelPermissions.add(ChannelPermissions.Write);
		channelPermissions.add(ChannelPermissions.Read);
		 
		permissions.put(this.channelName, channelPermissions);
		 
		// Send the authentication data to the Realtime.co server
		boolean result = Ortc.saveAuthentication(url,isCluster,this.authenticationToken,authenticationTokenIsPrivate,applicationKey,timeToLive,privateKey,permissions);
		this.authSaved = result;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public boolean isAuthSaved() {
		return authSaved;
	}

	@Override
	public String toString() {
		return "SimpleMessaging [channelName=" + channelName + ", authenticationToken=" + authenticationToken + ", authSaved" + authSaved + "]";
	}
}