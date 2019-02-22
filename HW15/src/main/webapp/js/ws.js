var webSocket = new WebSocket(getWsUrl());


webSocket.onmessage = function (message) {

    var resp = JSON.parse(message.data);
    if (resp['type'] == "AddUserResp") {
        drawAddUserResp(JSON.parse(resp.data), "addUserTable");
    }
    if (resp['type'] == "GetByIdResp") {
        drawAddUserResp(JSON.parse(resp.data), "getByIdTable");
    }
    if (resp['type'] == "GetAllResp") {
        drawAllUserResp(JSON.parse(resp.data), "getAllTable");
    }
};

webSocket.onerror = function (ws, ev) {
    alert(ev);
    alert(ws);
};

webSocket.onclose = function (ev) {
    alert(ev);
};


function drawAllUserResp(data, id) {
    document
    document.getElementById("count").innerText = "Всего пользователей: " + data.length;
    var div = document.getElementById(id);
    var labels1 = ['ID', 'Имя', 'Возраст', 'Адрес', 'Телефон'];
    buildTable(labels1, data, div);
    div.style.visibility = "visible";
};

function drawAddUserResp(user, id) {
    document.getElementById("userID").innerText = user.id;
    document.getElementById("userAge").innerText = user.age;
    document.getElementById("userName").innerText = user.name;
    document.getElementById("userStreet").innerText = user.address.street;
    document.getElementById("userPhone").innerText = user.phones[0].number;
    document.getElementById(id).style.visibility = "visible";
}


function wsSendMessage(message) {
    webSocket.send(message);
}

function sendLoginMessage(login) {
    var msg = new Object();
    msg.type = "login";
    msg.data = login;
    wsSendMessage(JSON.stringify(msg));
}

function sendAddUser(form) {
    document.getElementById("addUserTable").style.visibility = "hidden";
    var obj = {};
    var elements = form.querySelectorAll("input");
    for (var i = 0; i < elements.length; ++i) {
        var element = elements[i];
        var name = element.name;
        var value = element.value;

        if (name) {
            if (name == 'phones') {
                var phone = {};
                phone['number'] = value;
                obj[name] = [phone];
            } else {
                if (name == 'street') {
                    var address = {};
                    address['street'] = value;
                    obj['address'] = address;
                } else {
                    obj[name] = value;
                }
            }
        }
    }
    var msg = {};
    msg.type = "AddUserReq";
    msg.data = JSON.stringify(obj);
    wsSendMessage(JSON.stringify(msg));
    return false;
}

function sendGetByIdReq(id) {
    var element = id.querySelector("input");
    var value = element.value;

    var msg = {};
    msg.type = "GetByIdReq";
    msg.data = value;
    wsSendMessage(JSON.stringify(msg));
    return false;
}

function sendGetAllReq() {
    var msg = {};
    msg.type = "GetAllReq";
    waitForSocketConnection(webSocket, function () {
        console.log("message sent!!!");
        wsSendMessage(JSON.stringify(msg));
    });

    return false;
}

function getWsUrl() {
    var arr=window.location.pathname.toString().split("/");
    arr.pop();
    arr.push("ws");
    str=
        "ws://" + window.location.host + "/"
        + arr.join("/");

return str;
}


function waitForSocketConnection(socket, callback) {
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                console.log("Connection is made");
                if (callback != null) {
                    callback();
                }
                return;

            } else {
                console.log("wait for connection...");
                waitForSocketConnection(socket, callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}

function buildTable(labels, objects, container) {
    var table = document.createElement('table');
    var thead = document.createElement('thead');
    var tbody = document.createElement('tbody');

    var theadTr = document.createElement('tr');
    for (var i = 0; i < labels.length; i++) {
        var theadTh = document.createElement('th');
        theadTh.innerHTML = labels[i];
        theadTr.appendChild(theadTh);
    }
    thead.appendChild(theadTr);
    table.appendChild(thead);

    for (var j = 0; j < objects.length; j++) {
        var tbodyTr = document.createElement('tr');

        var tbodyTdID = document.createElement('td');
        var tbodyTdName = document.createElement('td');
        var tbodyTdAge = document.createElement('td');
        var tbodyTdAddress = document.createElement('td');
        var tbodyTdPhone = document.createElement('td');

        tbodyTdID.innerHTML = objects[j]['id'];
        tbodyTdName.innerHTML = objects[j]['name'];
        tbodyTdAge.innerHTML = objects[j]['age'];
        tbodyTdAddress.innerHTML = objects[j]['address'].street;
        var phoneList = document.createElement('ul');

        for (var k = 0; k < objects[j]['phones'].length; k++) {
            var li = document.createElement('li');
            li.innerText = objects[j]['phones'][k].number;
            phoneList.appendChild(li);
        }
        tbodyTdPhone.appendChild(phoneList);

        tbodyTr.appendChild(tbodyTdID);
        tbodyTr.appendChild(tbodyTdName);
        tbodyTr.appendChild(tbodyTdAge);
        tbodyTr.appendChild(tbodyTdAddress);
        tbodyTr.appendChild(tbodyTdPhone);

        tbody.appendChild(tbodyTr);

    }

    table.appendChild(tbody);

    table.setAttribute('border','1');

    container.appendChild(table);
}



