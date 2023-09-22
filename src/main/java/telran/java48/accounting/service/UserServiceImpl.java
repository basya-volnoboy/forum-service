package telran.java48.accounting.service;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java48.accounting.dao.UserRepository;
import telran.java48.accounting.dto.UserChangeRoleDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserUpdateDto;
import telran.java48.accounting.dto.exeptions.UserExistsExceprion;
import telran.java48.accounting.dto.exeptions.UserNotFoundException;
import telran.java48.accounting.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	final UserRepository userRepository;
	final ModelMapper modelMapper;
	
	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		
		if(userRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsExceprion();
		}
		User user = modelMapper.map(userRegisterDto, User.class);
		user.addRole("USER");
		userRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto login() {
		// TODO Auto-generated method stub
		//////////////////////////////////
		return null;
	}

	@Override
	public UserDto deleteUser(String login) {
		User user = userRepository.findById(login).orElseThrow(()-> new UserNotFoundException());
		userRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUpdateDto) {
		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String firstName = userUpdateDto.getFirstName();
		if (firstName != null) {
			user.setFirstName(firstName);
		}
		String lastName = userUpdateDto.getLastName();
		if (lastName != null) {
			user.setLastName(lastName);
		}
		userRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserChangeRoleDto changeRoleList(String login, String role, boolean isAddRole) {
		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		boolean res;
		if(isAddRole) {
			res = user.addRole(role);
		} else {
			res = user.deleteRole(role);
		}
		if(res) {
			userRepository.save(user);
		}
		return modelMapper.map(user, UserChangeRoleDto.class);
	}
//	@Override
//	public UserChangeRoleDto addRole(String login, String role) {
//		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
//		user.addRole(role);
//		userRepository.save(user);
//		return modelMapper.map(user, UserChangeRoleDto.class);
//	}
//
//	@Override
//	public UserChangeRoleDto deleteRole(String login, String role) {
//		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
//		user.deleteRole(role);
//		userRepository.save(user);
//		return modelMapper.map(user, UserChangeRoleDto.class);
//	}

	@Override
	public void changePassword(String login, String newPassword) {
		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		user.setPassword(newPassword);
		userRepository.save(user);
	}
	

	@Override
	public UserDto getUser(String login) {
		User user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(user, UserDto.class);
	}

}
