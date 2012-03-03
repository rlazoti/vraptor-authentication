package br.com.rlazoti.vraptor.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class AuthenticationControlTest {

  private AuthenticationControl authenticationControl;
  private HttpSession httpSession;
  private String object;

  @Before
  public void setUp() throws Exception {
    object = "Nick Fury";
    httpSession = mock(HttpSession.class);
    authenticationControl = new AuthenticationControl(httpSession);
  }

  @Test
  public void testAuthenticationControl() {
    assertNotNull(authenticationControl);
  }

  @Test
  public void testCreateSession() {
    authenticationControl.createSession(object);

    assertTrue(authenticationControl.isSessionCreated());
    assertEquals(object, authenticationControl.getObjectInTheSession());
  }

  @Test
  public void testDestroySession() {
    authenticationControl.createSession(object);
    authenticationControl.destroySession();

    assertFalse(authenticationControl.isSessionCreated());
    assertNull(authenticationControl.getObjectInTheSession());
  }

}
