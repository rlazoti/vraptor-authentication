VRaptor-Authentication
======================

It's a simple way to add an authentication method for VRaptor projects.

Requirements
============

VRaptor 3.x

Install
=======

To use VRaptor-Authentication in your vraptor project, you don't need to define anything.

You only need to add the jar to your project. :)

How it works
============

The VRaptor-Authentication will be find in all classes with the @Resource annotation a method annotated with the @Login annotation.

After that, for every request, it will check if there is a session created.

If the session wasn't created, the request will be redirected to the method with the @Login annotation or the request will be return a 401 (unauthorized) http status.

The VRaptor-Authentication won't check the request to methods that have the @Public annotation.

Usage
=====

Controller
----------

You should annotate only one method with the @Login annotation.

If the Vraptor-Authentication locate more than one method with @Login annotation, a VRaptorAuthenticationException will be thrown.

    @Resource
    public class WelcomeController {
      
        @Login
        @Get("/login")
        public void login() {
        }
        
    }

You can define methods that have public access with the @Public annotation.

In other words, a method with @Public annotation is accessible without the need for a session.
 
    @Resource
    public class WelcomeController {
      
        @Login
        @Get("/login")
        public void login() {
        }

        @Public
        @Get("/index")
        public void index() {
        }
        
    }

You can define two ways to treat an unauthorized request.


The first possibility (Default) is redirect the request to the @public method, and if you want this behavior you can add the value REDIRECT_TO_LOGIN to the attribute unauthorizedAction in the @Login annotation.

Remember that's the default behavior, so, if you don't add the unauthorizedAction attribute in the @Login annotation, it'll be automatic defined to REDIRECT_TO_LOGIN.

    @Resource
    public class WelcomeController {
      
        @Login(unauthorizedAction = UnauthorizedAction.REDIRECT_TO_LOGIN)
        @Get("/login")
        public void login() {
        }

    }


And the second possibility is return a 401 (unauthorized) status.
To do it, you just need to add the value RETURN_UNAUTHORIZED_STATUS to the attribute unauthorizedAction in the @Login annotation.

    @Resource
    public class WelcomeController {
      
        @Login(unauthorizedAction = UnauthorizedAction.RETURN_UNAUTHORIZED_STATUS)
        @Get("/login")
        public void login() {
        }

    }




Logic
-----

To add an user to a sesssion, you just need to add the user's instance or someone else to a new session like that:

    @Resource
    public class AuthenticationController {

        @Post("/login")
        public void login(final User user) {
          //validate the user information
          authenticationControl.createSession(user);
        }

    }

And to close the session.

    @Resource
    public class AuthenticationController {

        @Post("/login")
        public void login(final User user) {
          //validate the user information
          authenticationControl.createSession(user);
        }

        @Get("/logout")
        public void logout() {
          authenticationControl.destroySession();
        }

    }


Author
======

Rodrigo Lazoti - rodrigolazoti@gmail.com
