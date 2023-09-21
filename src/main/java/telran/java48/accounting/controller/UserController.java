package telran.java48.accounting.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import telran.java48.accounting.dto.UserChangeRoleDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserUpdateDto;
import telran.java48.accounting.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserController{

	final UserService userService;
	
	@PostMapping("/register")
	public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return userService.register(userRegisterDto);
	}

	@PostMapping("/login")
	public UserDto login() {
		// TODO Auto-generated method stub
		return null;
	}

	@DeleteMapping("/user/{login}")
	public UserDto deleteUser(@PathVariable String login) {
		return userService.deleteUser(login);
	}

	@PutMapping("/user/{login}")
	public UserDto updateUser(@PathVariable String login, UserUpdateDto userUpdateDto) {
		return userService.updateUser(login, userUpdateDto);
	}

	@PutMapping("/user/{login}/role/{role}")
	public UserChangeRoleDto addRole(@PathVariable String login, @PathVariable String role) {
		return userService.addRole(login, role);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public UserChangeRoleDto deleteRole(@PathVariable String login, @PathVariable String role) {
		return userService.deleteRole(login, role);
	}

	@PutMapping("/password")
	public void changePassword() {
		// TODO Auto-generated method stub
		userService.changePassword();
	}

	@GetMapping("/user/{login}")
	public UserDto getUser(@PathVariable String login) {
		return userService.getUser(login);
	}

}
