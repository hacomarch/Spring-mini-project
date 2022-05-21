package Miary.miniWeb.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {

    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
}
