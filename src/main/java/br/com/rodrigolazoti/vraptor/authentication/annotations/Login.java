package br.com.rodrigolazoti.vraptor.authentication.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

  public enum UnauthorizedAction {
    REDIRECT_TO_LOGIN,
    RETURN_UNAUTHORIZED_STATUS
  }
  public UnauthorizedAction unauthorizedAction() default UnauthorizedAction.REDIRECT_TO_LOGIN;

}
