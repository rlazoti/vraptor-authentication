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

If the session wasn't created, the request will be redirected to the method with the @Login annotation. 

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

In others words, a method with @Public annotation is accessible without the need for a session.
 
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
