package telran.java48.accounting.service;


import java.util.HashSet;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner{
	
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	
	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		
		if(userAccountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsExceprion();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.addRole("USER");
		String password= BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());//генерирует случайный стринг- соль на базе которого будет шифровать пароль
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

//	@Override
//	public UserDto login() {
//		// TODO Auto-generated method stub
//		//////////////////////////////////
//		return null;
//	}

	@Override
	public UserDto deleteUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(()-> new UserNotFoundException());
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String firstName = userEditDto.getFirstName();
		if (firstName != null) {
			userAccount.setFirstName(firstName);
		}
		String lastName = userEditDto.getLastName();
		if (lastName != null) {
			userAccount.setLastName(lastName);
		}
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRoleList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		boolean res;
		if(isAddRole) {
			res = userAccount.addRole(role.toUpperCase());
		} else {
			res = userAccount.deleteRole(role.toUpperCase());
		}
		if(res) {
			userAccountRepository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}


	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
	}
	

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!userAccountRepository.existsById("admin")) {
			String password = BCrypt.hashpw("admin", BCrypt.gensalt());
			Set<String> roleSet = new HashSet<>();
			roleSet.add("USER");
			roleSet.add("MODERATOR");
			roleSet.add("ADMINISTRATOR");
			UserAccount userAccount = new UserAccount("admin", password, "", "", roleSet);
			userAccountRepository.save(userAccount);
		}
		
	}

}
