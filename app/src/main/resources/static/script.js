var serverResponse='nmsg';

function sendPost(way,jsonMsg){
    let ajax1 = new XMLHttpRequest();
        ajax1.open('POST',way);
        ajax1.setRequestHeader("Content-type", "application/json");
        ajax1.onreadystatechange=function(){
        if(ajax1.readyState == 4){
            if(ajax1.status == 200){
                console.log(ajax1);
                serverResponse= ajax1.response;
            }
        }
    }
    ajax1.send(jsonMsg);
}

function getTokens(){
    let userEmail=document.getElementById('input_login').value;
    let userPassword=document.getElementById('input_pass').value;
    let requestObject={email:userEmail, password:userPassword};
    sendPost('/api/token',JSON.stringify(requestObject));
    while(serverResponse=='nmsg'){}
    let tokensObject=JSON.parse(serverResponse);
    serverResponse='nmsg';
    localStorage.setItem("chargeRefToken",tokensObject.accessToken);
    localStorage.setItem("chargeAccToken",tokensObject.refreshToken);
    checkToken();
}

function renderLoginPage(){
    let content=document.getElementById('content');
        content.innerHTML='<h2>Login page</h2>'+
         '<p>email:</p> <input type="text" id="input_login">'+
         '<p>password:</p><input type="password" id="input_pass">'+
         '<input type="button" value="Send" onclick="getTokens()">';
}

function checkToken(){
    let refreshToken=localStorage.getItem('chargeRefToken');
    let accessToken=localStorage.getItem('chargeAccToken');
    if(refreshToken==null || accessToken==null){
        let content=document.getElementById('content');
        renderLoginPage();
        console.log('Tokens not found');
    }else
    console.log('Yes');
}