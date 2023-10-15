package telran.java48.accounting.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import telran.java48.accounting.dto.RolesDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserEditDto;
import telran.java48.accounting.service.UserAccountService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController{

	final UserAccountService userAccountService;
	
	@PostMapping("/register")
	public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.register(userRegisterDto);
	}

	@PostMapping("/login")
	public UserDto login(Principal principal) { //это обьект который хранит имя пользователя
		
		return getUser(principal.getName());
	}

	@DeleteMapping("/user/{login}")
	public UserDto deleteUser(@PathVariable String login) {
		return userAccountService.deleteUser(login);
	}

	@PutMapping("/user/{login}")
	public UserDto updateUser(@PathVariable String login, @RequestBody UserEditDto userEditDto) {
		return userAccountService.updateUser(login, userEditDto);
	}

	@PutMapping("/user/{login}/role/{role}")
	public RolesDto addRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.changeRoleList(login, role, true);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public RolesDto deleteRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.changeRoleList(login, role, false);
	}

	@PutMapping("/password")
	public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
		userAccountService.changePassword(principal.getName(), newPassword);
	}

	@GetMapping("/user/{login}")
	public UserDto getUser(@PathVariable String login) {
		return userAccountService.getUser(login);
	}

}
