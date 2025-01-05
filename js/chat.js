var _userData;

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
		console.log(Data);
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
			console.log(_pfpPath);
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
}


Socket.onclose = function(){
	console.log("A SOCKET CONNECTION TO /ws HAS BEEN CLOSED");
}



window.Socket = Socket;