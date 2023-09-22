package telran.java48.accounting.service;

import telran.java48.accounting.dto.UserChangeRoleDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserUpdateDto;

public interface UserService {
	
	UserDto register(UserRegisterDto userRegisterDto);
	UserDto login();
	UserDto deleteUser(String login);
	UserDto updateUser(String login, UserUpdateDto userUpdateDto);
//	UserChangeRoleDto addRole(String login, String role);
//	UserChangeRoleDto deleteRole(String login, String role);
	public UserChangeRoleDto changeRoleList(String login, String role, boolean isAddRole);
	public void changePassword(String login, String newPassword);
	UserDto getUser(String login);
}
