package telran.java48.security.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java48.accounting.dao.UserAccountRepository;
import telran.java48.accounting.model.UserAccount;
import telran.java48.security.model.Role;
import telran.java48.security.model.User;

@Component
//@RequiredArgsConstructor
@Order(40)
public class DeleteUserFilter implements Filter {

	//final UserAccountRepository userAccountRepository;
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req; //такой кастинг для удобства доставания из реквеста и респонса заголовки
		HttpServletResponse response = (HttpServletResponse) resp;
	
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User user = (User) request.getUserPrincipal();
			//Principal principal = request.getUserPrincipal();
			String[] arr = request.getServletPath().split("/");
			String userName = arr[arr.length - 1];
			//UserAccount userAccount = userAccountRepository.findById(principal.getName()).get();
			if(!(user.getRoles().contains(Role.ADMINISTRATOR) 
					|| user.getName().equalsIgnoreCase(userName))) {
				response.sendError(403);
				return;
			}
			
//			String[] arr = request.getServletPath().split("/");
//			String user = arr[arr.length - 1];
//			if(!principal.getName().equalsIgnoreCase(user)) {
//				response.sendError(403);
//				return;
//			}
		}
		
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		
		return HttpMethod.DELETE.matches(method) && path.matches("/account/user/\\w+/?");
	}


	

};
