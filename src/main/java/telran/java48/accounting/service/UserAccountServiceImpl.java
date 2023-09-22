package telran.java48.accounting.service;


import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java48.accounting.dao.UserAccountRepository;
import telran.java48.accounting.dto.RolesDto;
import telran.java48.accounting.dto.UserDto;
import telran.java48.accounting.dto.UserRegisterDto;
import telran.java48.accounting.dto.UserEditDto;
import telran.java48.accounting.dto.exeptions.UserExistsExceprion;
import telran.java48.accounting.dto.exeptions.UserNotFoundException;
import telran.java48.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{
	
	final UserAccountRepository userRepository;
	final ModelMapper modelMapper;
	
	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		
		if(userRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsExceprion();
		}
		UserAccount user = modelMapper.map(userRegisterDto, UserAccount.class);
		user.addRole("USER");
		String password= BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());//генерирует случайный стринг- соль на базе которого будет шифровать пароль
		user.setPassword(password);
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
		UserAccount user = userRepository.findById(login).orElseThrow(()-> new UserNotFoundException());
		userRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userUpdateDto) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
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
	public RolesDto changeRoleList(String login, String role, boolean isAddRole) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		boolean res;
		if(isAddRole) {
			res = user.addRole(role);
		} else {
			res = user.deleteRole(role);
		}
		if(res) {
			userRepository.save(user);
		}
		return modelMapper.map(user, RolesDto.class);
	}


	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		user.setPassword(newPassword);
		userRepository.save(user);
	}
	

	@Override
	public UserDto getUser(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(user, UserDto.class);
	}

}
