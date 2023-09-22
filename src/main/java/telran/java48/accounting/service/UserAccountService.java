package telran.java48.accounting.service;

import telran.java48.accounting.dto.RolesDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserEditDto;

public interface UserAccountService {
	
	UserDto register(UserRegisterDto userRegisterDto);
	UserDto login();
	UserDto deleteUser(String login);
	UserDto updateUser(String login, UserEditDto userUpdateDto);
	public RolesDto changeRoleList(String login, String role, boolean isAddRole);
	public void changePassword(String login, String newPassword);
	UserDto getUser(String login);
}
