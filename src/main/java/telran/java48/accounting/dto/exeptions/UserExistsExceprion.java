package telran.java48.accounting.dto.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsExceprion extends RuntimeException{

	private static final long serialVersionUID = 1270809568161361058L;

}
