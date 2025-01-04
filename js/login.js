import { getCookie } from "./utils.js";

fetch("http://localhost:8080/api/v1/users", {
	method: "POST",
	credentials: "include"
})

function setup(){
	document.getElementById("LoginForm").addEventListener("submit", function(e){
		e.preventDefault();
		var _body = new FormData(this);
		fetch("http://localhost:8080/api/v1/login", {
			method: "POST",
			headers: {
				"X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
			},
			credentials: "include",
			body: _body
		})
		.then (Response => {
			if (Response.ok){
				window.location.href = window.location.origin + "/html/home.html";
			}
			return Response.text();
		})
		.then (Data => {
			console.log(Data);
		})
	})
	
	document.getElementById("SignupForm").addEventListener("submit", function(e){
		e.preventDefault();
		var _body = new FormData(this);
		fetch("http://localhost:8080/api/v1/register", {
			method: "POST",
			headers: {
				"X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
			},
			credentials: "include",
			body: _body
		})
		.then (Response => {
			return Response.text();
		})
		.then (Data => {
			console.log(Data);
		})
	})
	
	document.querySelector("#SignupText").addEventListener("click", function(){
		document.querySelector(".wrapper").style.transform = "rotateY(0deg)";
	})
	
	document.querySelector("#LoginText").addEventListener("click", function(){
		document.querySelector(".wrapper").style.transform = "rotateY(180deg)";
	})
}

setup();

function changeImage(event) {
	const file = event.target.files[0];

	if (file && file.type.startsWith("image/")) {
		const reader = new FileReader();

		reader.onload = function(e) {
			const imageElement = document.getElementById("pfplabel");
			imageElement.style.backgroundImage = `url(${e.target.result})`;
		};

		reader.readAsDataURL(file);
	} else {
		alert("Please upload a valid image.");
	}
}

window.changeImage = changeImage;