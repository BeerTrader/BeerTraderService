package com.db;

import org.apache.commons.lang.StringUtils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import com.auth.token.TokenManager;
import com.exceptions.DuplicateUserException;
import com.exceptions.UserNotAuthorizedException;
import com.exceptions.UserNotFoundException;
import com.factory.LabelFactory;
import com.objects.domain.User;

public class UserDB {
    private UserDB() {};
    
	public static boolean userExists(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> iterable = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", username).iterator();
			boolean result = iterable.hasNext();
			iterable.close();
			tx.success();
			return result;
		}
	}
	
	public static User getUser(String username) throws UserNotFoundException {
		if (UserDB.userExists(username)) {
			try (Transaction tx = DataManager.getInstance().beginTx()) {
				Node userNode = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", username).iterator().next();
				User user = convertToUser(userNode);
				tx.success();
				return user;
			}
		} else {
			throw new UserNotFoundException(username);
		}
	}
	
	public static Node getUserNode(String token) throws UserNotFoundException {
		User u = TokenManager.getUser(token);
		if (UserDB.userExists(u.getUsername())) {
			try (Transaction tx = DataManager.getInstance().beginTx()) {
				Node userNode = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", u.getUsername()).iterator().next();
				tx.success();
				return userNode;
			}
		} else {
			throw new UserNotFoundException(u.getUsername());
		}
	}	
	
	public static User authenticateUser(String username, String password) throws UserNotAuthorizedException {
		User authorizedUser;
		try {
			authorizedUser = UserDB.getUser(username);
		} catch (UserNotFoundException e) {
			throw new UserNotAuthorizedException(username);
		}
		if (StringUtils.equals(authorizedUser.getPassword(), password)) {
			return authorizedUser;
		} else {
			throw new UserNotAuthorizedException(username);
		}
	}	
	
	public static void registerUser(String username, String password, double latitude, double longitude) throws DuplicateUserException {
		if (UserDB.userExists(username)) {
			throw new DuplicateUserException(username);
		}
		
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node newUserNode = DBHelper.createNodeWithUniqueId(LabelFactory.getLabel("USER"));
			newUserNode.setProperty("username", username);
			newUserNode.setProperty("password", password);
			newUserNode.setProperty("latitude", latitude);
			newUserNode.setProperty("longitude", longitude);
			tx.success();
		}
	}
	
	public static User convertToUser(Node userNode) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			User u = new User(Long.parseLong(userNode.getProperty("id").toString()), userNode.getProperty("username").toString(), userNode.getProperty("password").toString(), Double.parseDouble(userNode.getProperty("latitude").toString()), Double.parseDouble(userNode.getProperty("longitude").toString()));
			tx.success();
			return u;
		}
	}
	
	public static User convertToSecureUser(Node userNode) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			User u = new User(Long.parseLong(userNode.getProperty("id").toString()), userNode.getProperty("username").toString(), null, 0.0, 0.0);
			tx.success();
			return u;
		}
	}	
}