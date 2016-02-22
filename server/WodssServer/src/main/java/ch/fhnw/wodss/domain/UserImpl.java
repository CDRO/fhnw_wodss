package ch.fhnw.wodss.domain;

class UserImpl implements User {
	
	private String name;
	private String email;
	
	UserImpl(){
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

}
