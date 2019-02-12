/*Used following tutorial series' in creation of this html page:
https://www.youtube.com/watch?v=B-G9283Ssd4&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh
https://www.youtube.com/watch?v=tAN244LVW-s&list=PLk7v1Z2rk4hgNu5zXbGctiCWYebxjdeey*/

//file used for generation of access token and posting request via localhost server
//localised api
//use of googleapis, body-parser, express and request dependencies

var {google} = require('googleapis');
var MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
var SCOPES = [MESSAGING_SCOPE];

var express = require('express');
var app= express();

var bodyParser = require('body-parser');
var router = express.Router();

var request = require('request');
var port = 8085;

app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

router.post('/send', function(req, res){

    getAccessToken().then(function(access_token){

        var title = req.body.title;
        var body = req.body.body;
        var token = req.body.token;

        request.post({
            headers:{
                Authorization: 'Bearer '+access_token
            },
            url: "https://fcm.googleapis.com/v1/projects/finalyearproject-c85ba/messages:send",
            body: JSON.stringify(
                {
                    "message":{
                      "token" : token,
                      "notification" : {
                            "body" : body,
                            "title" : title,
                        }
                     }
                  }
            )
        }, function(error, response, body){
            res.end(body);
            console.log(body);
        });

    });
});

app.use('/api', router);

app.listen(port, function(){
    console.log("Server listening "+ port);
});

//function to generate access token
function getAccessToken(){
    return new Promise(function(resolve, reject){
        var key = require("./service-account.json");
        var jwtClient = new google.auth.JWT(
            key.client_email,
            null,
            key.private_key,
            SCOPES,
            null
        );
        jwtClient.authorize(function(err, tokens){
            if(err){
                reject(err);
                return; 
            }
            resolve(tokens.access_token);
        });
    });
}