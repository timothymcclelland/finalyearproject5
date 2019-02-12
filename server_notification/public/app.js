/*Used following tutorial series' in creation of this html page:
https://www.youtube.com/watch?v=B-G9283Ssd4&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh
https://www.youtube.com/watch?v=tAN244LVW-s&list=PLk7v1Z2rk4hgNu5zXbGctiCWYebxjdeey*/

//config file taken from my own project workspace on firebase website
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

//method to authenticate user against data stored in Firebase RealTime Database
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
