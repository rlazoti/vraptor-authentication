package br.com.rodrigolazoti.vraptor.authentication.interceptors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.view.LogicResult;
import br.com.caelum.vraptor.view.Results;
import br.com.rodrigolazoti.vraptor.authentication.AuthenticationControl;
import br.com.rodrigolazoti.vraptor.authentication.annotations.Login;
import br.com.rodrigolazoti.vraptor.authentication.components.LoginHandler;
import br.com.rodrigolazoti.vraptor.authentication.interceptors.AuthenticationInterceptor;

public class AuthenticationInterceptorTest {

  private AuthenticationControl authenticationControl;
  private AuthenticationInterceptor interceptor;
  private InterceptorStack stack;
  private ResourceMethod method;
  private LoginHandler handler;
  private Result result;

  @Resource
  public class AuthenticationController {

    @Get
    public void index() {
    }

    @Login
    @Get
    public void login() {
    }

    @Get
    public void logout() {
    }

  };

  @Before
  public void setUp() throws Exception {
    result = mock(MockResult.class);
    stack = mock(InterceptorStack.class);
    method = mock(ResourceMethod.class);

    handler = new LoginHandler();
    handler.handle(AuthenticationController.class);

    authenticationControl = new AuthenticationControl(mock(HttpSession.class));
    interceptor = new AuthenticationInterceptor(authenticationControl, handler, result);
  }

  @Test
  public void acceptARequestWithACreatedSession() throws Exception {
    givenThereIsAnObjectInTheSession();
    whenInterceptOccurs();
    verify(stack).next(any(ResourceMethod.class), anyObject());
  }

  @Test
  public void rejectARequestWithoutASession() throws Exception {
    AuthenticationController controller = mockAuthenticationController();
    givenThereIsNotASession();
    whenInterceptOccurs();
    verify(controller).login();
  }

  @Test
  public void shouldNotBypassLoginMethodInAuthenticationController() throws Exception {
    ResourceMethod resourceMethod = obtainMethodByName(AuthenticationController.class, "login");
    assertFalse(interceptor.accepts(resourceMethod));
  }

  @Test
  public void shouldBypassLogoutMethodAuthenticationController() throws Exception {
    ResourceMethod resourceMethod = obtainMethodByName(AuthenticationController.class, "logout");
    assertTrue(interceptor.accepts(resourceMethod));
  }

  private ResourceMethod obtainMethodByName(Class<?> clazz, String methodName) {
    Method[] methods = clazz.getDeclaredMethods();
    Method methodFound = null;

    for (Method method : methods) {
      if (methodName.equalsIgnoreCase(method.getName())) {
        methodFound = method;
        break;
      }
    }

    return new DefaultResourceMethod(new DefaultResourceClass(clazz), methodFound);
  }

  private AuthenticationController mockAuthenticationController() {
    AuthenticationController controller = mock(AuthenticationController.class);
    LogicResult logic = mock(LogicResult.class);
    when(result.use(Results.logic())).thenReturn(logic);
    when(result.use(Results.logic()).redirectTo(AuthenticationController.class)).thenReturn(controller);

    return controller;
  }

  private void givenThereIsAnObjectInTheSession() {
    Object user = "Sub-Zero";
    authenticationControl.createSession(user);
  }

  private void givenThereIsNotASession() {
    authenticationControl.destroySession();
  }

  private void whenInterceptOccurs() {
    interceptor.intercept(stack, method, null);
  }

}
