package br.com.rlazoti.vraptor.authentication.interceptors;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;
import br.com.rlazoti.vraptor.authentication.AuthenticationControl;
import br.com.rlazoti.vraptor.authentication.annotations.Login;
import br.com.rlazoti.vraptor.authentication.annotations.Public;
import br.com.rlazoti.vraptor.authentication.components.LoginHandler;

@Intercepts
public class AuthenticationInterceptor implements Interceptor {

  private final AuthenticationControl authenticationControl;
  private final LoginHandler loginHandler;
  private final Result result;

  public AuthenticationInterceptor(AuthenticationControl authenticationControl, LoginHandler loginHandler, Result result) {
    this.authenticationControl = authenticationControl;
    this.loginHandler = loginHandler;
    this.result = result;
  }

  @Override
  public boolean accepts(ResourceMethod method) {
    return !(method.containsAnnotation(Public.class) || method.containsAnnotation(Login.class));
  }

  @Override
  public void intercept(InterceptorStack stack, ResourceMethod method, Object object) throws InterceptionException {
    if (!authenticationControl.isThereAnObjectInTheSession() && loginHandler.isThereALoginActionStored()) {
      Object instance = result.use(Results.logic()).redirectTo(loginHandler.getClassOfController());
      new Mirror().on(instance).invoke().method(loginHandler.getMethodOfAction()).withoutArgs();
    }
    else {
      stack.next(method, object);
    }
  }
}
