package ch.fhnw.wodss.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	 * Default constructor.
	 */
	Token(){
		super();
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

}
