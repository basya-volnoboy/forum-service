package telran.java48.accounting.model;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
public class UserAccount {
	@Id
	String login;
	@Setter
	String password;
	@Setter
    String firstName;
	@Setter
    String lastName;
    Set<String> roles;
    
    
	public UserAccount() {
		roles = new HashSet<>();
	}

	public UserAccount(String login, String password, String firstName ,String lastName, Set<String> roles) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
	}

	public boolean addRole(String role) {
		return roles.add(role);
	}
	
	public boolean deleteRole(String role) {
		return roles.remove(role);
	}
	
}
