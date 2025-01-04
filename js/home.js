import { getCookie } from "./utils.js";

fetch("http://localhost:8080/api/v1/users", {
	method: "GET",
	credentials: "include"
}
)
.then(Response => {
	if (!Response.ok)
		window.location.href = window.location.origin + "/html/login.html";
	return Response.json();
})
.then(Data => {
	console.log(Data);
	var _userWrapper = document.querySelector(".usersWrapper");
	for (var i = 0; i < Data.length; i++){
		_userWrapper.innerHTML += `<div class="profile-container">
									<img src="${"http://localhost:8888/" + Data[i].pfpPath.replace("uploads/", "")}" 
									alt="Profile Picture" class="profile-picture">
									<div class="profile-info">
										<h2>${Data[i].username}</h2>
										<button class="btn btn-outline-primary" name="${Data[i].username}" onclick="startMessaging(this)">Message</button>
									</div>
								</div>`;
	}
})

function startMessaging(e){
	console.log(e.name);
	window.location.href = window.location.origin + `/html/chat.html?user=${e.name}`;
}

window.startMessaging = startMessaging;