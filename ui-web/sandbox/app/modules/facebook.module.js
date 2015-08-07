define(['facebook'], function(){
  (function(){


    console.log("WAAT");
    FB.init({
      appId      : '1597342647199773',
      version    : 'v2.4',
      /*
       Set if you want to check the authentication status
       at the start up of the app
       */

      status: true,

      /*
       Enable cookies to allow the server to access
       the session
       */

      cookie: true,

      /* Parse XFBML */

      xfbml: true
    });
    FB.getLoginStatus(function(response) {
      console.log('wat'+response);
    });

  })();
});