package br.com.rodrigolazoti.vraptor.authentication;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@SessionScoped
@Component
public class AuthenticationControl implements Serializable {

  private static final long serialVersionUID = -8162257388304382662L;
  private Logger logger = LoggerFactory.getLogger(AuthenticationControl.class);

  private Object object;
  private HttpSession httpSession;

  public AuthenticationControl(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  public void createSession(Object object) {
    this.object = object;
    logger.debug(String.format("Session created. Add the object (%s) to the session", object));
  }

  public void destroySession() {
    this.object = null;
    httpSession.invalidate();
    logger.debug("Session destroyed");
  }

  public boolean isThereAnObjectInTheSession() {
    return object != null;
  }

  public Object getObjectInTheSession() {
    return object;
  }

}
