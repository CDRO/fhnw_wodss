package ch.fhnw.wodss.security;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.fhnw.wodss.domain.User;

public class Token {

	/**
	 * The token id;
	 */
	private String id;

	/**
	 * The time when the token was created in millis.
	 */
	private Long createdAt;

	/**
	 * The time to live in millis.
	 */
	private Long timeToLive;

	/**
	 * The user to which this token is bound.
	 */
	private User user;

	/**
	 * Default constructor.
	 */
	public Token() {
		super();
	}

	public Token(String id) {
		this.id = id;
	}

	/**
	 * Return whether this token is expired or not.
	 * 
	 * @return whether this token is expired or not.
	 */
	@JsonIgnore
	public boolean isExpired() {
		Long current = System.currentTimeMillis();
		return current >= createdAt + timeToLive;
	}

	/**
	 * Gets the token id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets th token id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the creation time in millis.
	 * 
	 * @return the createdAt
	 */
	public Long getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the creation time in millis.
	 * 
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the time to live in millis.
	 * 
	 * @return the time to live.
	 */
	public Long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * Sets the time to live in millis.
	 * 
	 * @param expiringAt
	 *            the time to live to set
	 */
	public void setTimeToLive(Long expiringAt) {
		this.timeToLive = expiringAt;
	}

	/**
	 * Gets the user bound to this token.
	 * 
	 * @return the user bound to this token.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user bound to this token.
	 * 
	 * @param user
	 *            the user bound to this token.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Token)) {
			return false;
		}
		Token token = (Token) object;
		return token.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(createdAt).append(timeToLive).append(user).toHashCode();
	}

}
