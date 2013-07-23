package br.com.rodrigolazoti.vraptor.authentication.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.StereotypeHandler;
import br.com.rodrigolazoti.vraptor.authentication.annotations.Login;

@ApplicationScoped
@Component
public class LoginHandler implements StereotypeHandler {

  private Logger logger = LoggerFactory.getLogger(LoginHandler.class);

  private Class<?> classOfLoginController;
  private Method methodOfLoginAction;

  @Override
  public Class<? extends Annotation> stereotype() {
    return Resource.class;
  }

  @Override
  public void handle(Class<?> resource) {
    for (Method method : resource.getMethods()) {
      searchForLoginActionAndStore(resource, method);
    }

    //ifThereIsNotAnyLoginActionThrowAnException();
  }

  public Class<?> getClassOfController() {
    return classOfLoginController;
  }

  public Method getMethodOfAction() {
    return methodOfLoginAction;
  }

  public boolean isThereALoginActionStored() {
    return classOfLoginController != null && methodOfLoginAction != null;
  }

  private void searchForLoginActionAndStore(Class<?> resource, Method method) {
    if (method.isAnnotationPresent(Login.class)) {
      ifThereAreMoreThanOneLoginActionThrowAnException();
      storeTheLoginAction(resource, method);
      logger.debug(String.format("Login annotation detected at %1$s.%2$s", classOfLoginController.getName(), methodOfLoginAction.getName()));
    }
  }

  private void storeTheLoginAction(Class<?> resource, Method method) {
    classOfLoginController = resource;
    methodOfLoginAction = method;
  }

  private void ifThereAreMoreThanOneLoginActionThrowAnException() {
    if (classOfLoginController != null && methodOfLoginAction != null) {
      storeTheLoginAction(null, null);
      throw new VraptorAuthenticationException("There are more than one method with the @Login annotation.");
    }
  }

  private void ifThereIsNotAnyLoginActionThrowAnException() {
    if (classOfLoginController == null || methodOfLoginAction == null) {
      storeTheLoginAction(null, null);
      throw new VraptorAuthenticationException("There isn't any method with the @Login annotation.");
    }
  }

}
