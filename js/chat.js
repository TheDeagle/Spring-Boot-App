function fetchUrl(){
	var _url = new URL(window.location.href);
	var _params = new URLSearchParams(_url.search);
	console.log(_params.get("user"));
}

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