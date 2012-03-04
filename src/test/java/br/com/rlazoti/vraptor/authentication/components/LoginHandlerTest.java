package br.com.rlazoti.vraptor.authentication.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.Resource;
import br.com.rlazoti.vraptor.authentication.annotations.Login;

public class LoginHandlerTest {

  private LoginHandler handler;

  @Resource
  class ControllerWithLogin {

    @Login
    public void login() {
    }

    public void index() {
    }

  };

  @Resource
  class ControllerWithTwoLogins {

    @Login
    public void login1() {
    }

    @Login
    public void login2() {
    }

    public void index() {
    }

  };

  @Resource
  class ControllerWithoutLogin {

    public void index() {
    }

  };

  @Before
  public void setUp() throws Exception {
    handler = new LoginHandler();
  }

  @Test
  public void testStereotype() {
    assertEquals(Resource.class, handler.stereotype());
  }

  @Test
  public void testHandleAValidResource() throws SecurityException, NoSuchMethodException {
    handler.handle(ControllerWithLogin.class);

    assertTrue(handler.isThereALoginActionStored());
    assertEquals(ControllerWithLogin.class, handler.getClassOfController());
    assertEquals(ControllerWithLogin.class.getMethod("login", null), handler.getMethodOfAction());
  }

  @Test(expected = VraptorAuthenticationException.class)
  public void testHandleAResourceWithMoreThanOneLoginAction() {
    handler.handle(ControllerWithTwoLogins.class);

    fail("It didn't throw the expected exception.");
  }

  @Test(expected = VraptorAuthenticationException.class)
  public void testHandleAResourceWithoutALoginAction() {
    handler.handle(ControllerWithoutLogin.class);

    fail("It didn't throw the expected exception.");
  }

}
