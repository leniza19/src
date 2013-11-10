package mathchem.data;

public class User {
	private Long id;
	private String username;
	private String password;
	private Long permission;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getPermission() {
		return permission;
	}
	public void setPermission(Long permission) {
		this.permission = permission;
	}
	
}
