var _userData;
var _chatterData;

function fetchProfile(){
	fetch("http://localhost:8080/api/v1/profile", {
		credentials: "include"
	})
	.then(Response => {
		console.log(Response.status);
		if (!Response.ok)
			window.location.href = window.location.origin + "/html/home.html";
		return Response.json();
	})
	.then(Data => {
		_userData = Data;
	})
}

function fetchUrl(){
	var _url = new URL(window.location.href);
	var _params = new URLSearchParams(_url.search);
	fetch(`http://localhost:8080/api/v1/users/${_params.get("user")}`, {
		method: "GET",
		credentials: "include"
	})
	.then(Response => {
		if (!Response.ok)
			window.location.href = window.location.origin + "/html/home.html";
		return Response.json();
	})
	.then(Data => {
		_chatterData = Data;
		document.querySelector(".title").innerText = Data[0].username;
		for (let i = 0; i < Data[1].length; i++){
			var _leftRight = Data[1][i].receiverUsername == _userData.username ? "left" : "right"; 
			document.querySelector(".messages").innerHTML += ` <li class="message ${_leftRight} appeared">
																	<div class="avatar avatar${i}"></div>
																	<div class="text_wrapper">
																		<div class="text">${Data[1][i].message}</div>
																	</div>
																</li>`;
			var _pfp = document.querySelector(`.avatar${i}`);
			var _pfpPath = Data[1][i].receiverUsername == _userData.username ? Data[0].pfpPath : _userData.pfpPath; 
			_pfp.style.backgroundImage = `url("http://localhost:8888/${_pfpPath.replace("uploads/", "")}")`;
		}
	})
}

fetchProfile();
fetchUrl();

var Socket = new WebSocket("ws://localhost:8080/ws");

Socket.onopen = function(){
	console.log("A SOCKET CONNECTION TO /ws HAS BEEN ESTABLISHED");
}

Socket.onmessage = function(e){
	console.log(e.data);
	var _json = JSON.parse(e.data);
	
}


Socket.onclose = function(){
	console.log("A SOCKET CONNECTION TO /ws HAS BEEN CLOSED");
}


window.Socket = Socket;

document.querySelector(".fa-house").addEventListener("click", function()
{
	console.log("wtf");
	window.location.href = window.location.origin + "/html/home.html";
})

document.addEventListener("keypress", function(e){
	if (e.key == "Enter"){
		console.log(_chatterData[0].username);
		var _msg = document.querySelector(".message_input").value;
		Socket.send(JSON.stringify({
			"receiverUsername": _chatterData[0].username,
			"message": _msg
		}));
		this.querySelector(".message_input").value = "";
		var _rand = parseInt(Math.random() * 1000);
		document.querySelector(".messages").innerHTML += ` <li class="message right appeared">
																	<div class="avatar avatar${_rand}"></div>
																	<div class="text_wrapper">
																		<div class="text">${_msg}</div>
																	</div>
																</li>`;
		document.querySelector(`.avatar${_rand}`).style.backgroundImage = `url("http://localhost:8888/${_userData.pfpPath.replace("uploads/", "")}")`;	
	}
});