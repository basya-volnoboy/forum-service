package telran.java48.accounting.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
public class UserChangeRoleDto {
	String login;
    @Singular
    Set<String> roles;
}
