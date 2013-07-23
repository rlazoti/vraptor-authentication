package br.com.rodrigolazoti.vraptor.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import br.com.rodrigolazoti.vraptor.authentication.AuthenticationControl;

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

    assertTrue(authenticationControl.isThereAnObjectInTheSession());
    assertEquals(object, authenticationControl.getObjectInTheSession());
  }

  @Test
  public void testDestroySession() {
    authenticationControl.createSession(object);
    authenticationControl.destroySession();

    assertFalse(authenticationControl.isThereAnObjectInTheSession());
    assertNull(authenticationControl.getObjectInTheSession());
  }

  @Test
  public void testSerialization() throws IOException {
    authenticationControl.createSession(object);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);

    oos.writeObject(authenticationControl);
  }

}
