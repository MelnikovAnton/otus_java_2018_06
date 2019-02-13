var webSocket = new WebSocket(getWsUrl());

webSocket.onmessage = function(message){

    var resp =JSON.parse(message.data);
    if (resp['type'] == "AddUserResp"){
        drawAddUserResp(JSON.parse(resp.data),"addUserTable");
    }
    if (resp['type'] == "GetByIdResp"){
        drawAddUserResp(JSON.parse(resp.data),"getByIdTable");
    }
};

function drawAddUserResp(user,id) {
    document.getElementById("userID").innerText = user.id;
    document.getElementById("userAge").innerText = user.age;
    document.getElementById("userName").innerText = user.name;
    document.getElementById("userStreet").innerText = user.address.street;
    document.getElementById("userPhone").innerText = user.phones[0].number;
    document.getElementById(id).style.visibility = "visible";
}


function wsSendMessage(message){
    alert(message)
    webSocket.send(message);
}

function sendLoginMessage(login) {
    var msg = new Object();
       msg.type="login";
       msg.data=login;
    wsSendMessage(JSON.stringify(msg));
}

function sendAddUser(form) {
    document.getElementById("addUserTable").style.visibility = "hidden";
    var obj = {};
    var elements = form.querySelectorAll( "input" );
    for( var i = 0; i < elements.length; ++i ) {
        var element = elements[i];
        var name = element.name;
        var value = element.value;

        if (name) {
            if (name=='phones'){
                var phone = {};
                phone['number']=value;
                obj[name]=[phone];
            }else {
                if (name=='street'){
                    var address = {};
                    address['street']=value;
                    obj['address']=address;
                }else {
                    obj[name] = value;
                }
            }
        }
    }
    var msg = {};
    msg.type="AddUserReq";
    msg.data=JSON.stringify(obj);
    wsSendMessage(JSON.stringify(msg));
    return false;
}

function sendGetByIdReq(id) {
   var element= id.querySelector("input");
    var value = element.value;

    var msg = new Object();
    msg.type="GetByIdReq";
    msg.data=value;
    wsSendMessage(JSON.stringify(msg));
}

function getWsUrl(){
    return "ws://"+window.location.host + "/"
        +window.location.pathname.toString().split("/")[1]
        +"/ws";

}




