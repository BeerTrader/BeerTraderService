package com.db;

import org.apache.commons.lang.StringUtils;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import com.exceptions.DuplicateUserException;
import com.exceptions.UserNotAuthorizedException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.User;

public class UserDB {
	private static Label userLabel = DynamicLabel.label("User");
	
	public static boolean userExists(String username) {
		if (StringUtils.isEmpty(username))
			return false;
		
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> iterable = DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "username", username).iterator();
			boolean result = iterable.hasNext();
			iterable.close();
			tx.success();
			return result;
		}
	}
	
	public static User getUser(String username) throws UserNotFoundException {
		if (UserDB.userExists(username)) {
			try (Transaction tx = DataManager.getInstance().beginTx()) {
				Node userNode = DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "username", username).iterator().next();
				User user = new User(userNode.getId(),(String) userNode.getProperty("username"), (String) userNode.getProperty("password"));
				tx.success();
				return user;
			}
		}
		else {
			throw new UserNotFoundException(username);
		}
	}
	
	public static User authenticateUser(String username, String password) throws UserNotAuthorizedException {
		User authorizedUser;
		try {
			authorizedUser = UserDB.getUser(username);
		}
		catch (UserNotFoundException e) {
			throw new UserNotAuthorizedException(username);
		}
		
		if (StringUtils.equals(authorizedUser.getPassword(), password)) {
			return authorizedUser;
		}
		else {
			throw new UserNotAuthorizedException(username);
		}
	}	
	
	public static void registerUser(String username, String password) throws DuplicateUserException {
		if (UserDB.userExists(username))
			throw new DuplicateUserException(username);
		
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node newUserNode = DataManager.getInstance().createNode(userLabel);
			newUserNode.setProperty("username", username);
			newUserNode.setProperty("password", password);
			tx.success();
		}
	}
}