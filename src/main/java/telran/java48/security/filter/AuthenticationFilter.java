package telran.java48.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java48.accounting.dao.UserAccountRepository;
import telran.java48.accounting.dto.exeptions.UserNotFoundException;
import telran.java48.accounting.model.UserAccount;
import telran.java48.security.context.SecurityContext;
import telran.java48.security.model.Role;
import telran.java48.security.model.User;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	final UserAccountRepository userAccountRepository;
	final SecurityContext securityContext;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req; //такой кастинг для удобства доставания из реквеста и респонса заголовки
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String sessionId = request.getSession().getId();//создаем сессию для данного юзера авторизованного
			User user = securityContext.getUserBySessionId(sessionId);
			if(user == null) {
				try {
					String[] credentials = getCredentials(request.getHeader("Authorization"));
					UserAccount userAccount = userAccountRepository.findById(credentials[0])
							.orElseThrow(RuntimeException::new);
					if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
						throw new RuntimeException();
					}
					Set<Role> roles = userAccount.getRoles()
								.stream()
								.map(r -> Role.valueOf(r.toUpperCase()))
								.collect(Collectors.toSet());
					user = new User(userAccount.getLogin(), roles);
					securityContext.addUserSession(sessionId, user);
				} catch (Exception e) {
					response.sendError(401);
					return;
				} 
			}
			request = new WrappedRequest(request, user.getName(), user.getRoles());	
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		
		return !(
				(HttpMethod.POST.matches(method) && path.matches("/account/register/?"))
				|| path.matches("/forum/posts/\\w+(/\\w+)?/?")
				);
	}

	private String[] getCredentials(String header) { //вернется масив [login:pasword]
		String token = header.substring(6);
		String decode = new String(Base64.getDecoder().decode(token)); //все что в скобках возвращает масив байт - а у стринга есть конструктор который принимает масив байт и возвращает стринг
		
		return decode.split(":");
	}
	
	private class WrappedRequest extends HttpServletRequestWrapper{
		String login;
		Set<Role> roles;

		public WrappedRequest(HttpServletRequest request, String login, Set<Role> roles) {
			super(request);
			this.login = login;
			this.roles = roles;
		}
		
		@Override
		public Principal getUserPrincipal() { //это функциональный интеерфейс у него только 1 абстрактный метод гет нейм
			//return ()-> login; //равносильно что return new Principal()...
		return new User(login, roles);
		}
	}

}

//фильтр перехватыывает абсолютно все запросы в апликации и делает с ними какое-то действие и передает дальше