// Initialize Firebase
var config = {
    apiKey: "AIzaSyAAjX5DJkb13CJRRKc5eUMUJlqV8txQlk4",
    authDomain: "finalyearproject-c85ba.firebaseapp.com",
    databaseURL: "https://finalyearproject-c85ba.firebaseio.com",
    projectId: "finalyearproject-c85ba",
    storageBucket: "finalyearproject-c85ba.appspot.com",
    messagingSenderId: "248196793831"
  };
  firebase.initializeApp(config);

    firebase.auth.Auth.Persistence.LOCAL; 

    $("#btn-login").click(function(){
        
        var email = $("#email").val();
        var password = $("#password").val(); 

        var result = firebase.auth().signInWithEmailAndPassword(email, password);
    
        result.catch(function(error){
            var errorCode = error.code; 
            var errorMessage = error.message; 

            console.log(errorCode);
            console.log(errorMessage);
        });

    });

    $("#btn-logout").click(function(){
        firebase.auth().signOut();
    });

    function switchView(view){
        $.get({
            url:view,
            cache: false,  
        }).then(function(data){
            $("#container").html(data);
        });
    }