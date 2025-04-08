import { useRef, useState } from 'react';
import '../login.css'
import Navbar from './Navbar';
import "@fontsource/source-sans-pro";
import { useNavigate } from 'react-router-dom';

export default function Login(){
	const wrapper = useRef(null);
	const [state, setState] = useState(false);
	const rotate = () => {
		if (!state)
			wrapper.current.style.transform = "rotateY(0deg)";
		else
			wrapper.current.style.transform = "rotateY(180deg)";
		setState(state => !state);
	}


	var lUsername = useRef(null);
	var lPassword = useRef(null);
	var navigate = useNavigate();
	var Login = (e) => {
		e.preventDefault();
		var form = new FormData();
		form.append("username", lUsername.current.value);
		form.append("password", lPassword.current.value);
		fetch("http://localhost:8080/api/users/login", {
			method: "POST",
			credentials: "include",
			body: form
		})
		.then(Response => {
			if (Response.ok)
				navigate("/");
			return Response.text();
		})
		.then(Data => {
			console.log(Data);
		})
		.catch(Error => {
			console.log(Error);
		})
	}
	return (
		<div id="login">
			<Navbar login="true" ></Navbar>
			<div class="wrapper" ref={wrapper}>
				<div id="front">
					<div class="title">
						Login
					</div>
					<form id="LoginForm" onSubmit={Login}>
						<div style={{display: "flex", alignItems: "center", justifyContent: "center", height: "12dvh"}}>
							<h3 style={{fontSize: "40px", fontFamily: 'sans serif', fontWeight: 200}}>Welcome back!</h3>
						</div>
						<div class="field">
							<input type="text" autoComplete='off' ref={lUsername} required></input>
							<label>Username</label>
						</div>
						<div class="field">
							<input type="password" autoComplete='off' ref={lPassword} required></input>
							<label>Password</label>
						</div>
						<div class="field">
							<input type="submit" value="Login"></input>
						</div>
						<div class="signup-link">
							Not a member? <a onClick={rotate} id="SignupText">Signup now</a>
						</div>
					</form>
				</div>
				<div id="back">
					<div class="title">
						Signup
					</div>
					{/* <form id="SignupForm" onSubmit={Signup} autoComplete='off'>
						<input type="file" name="pfp" id="pfp" onchange="changeImage(event)"></input>
						<label for="pfp" id="pfplabel">
							<div><i class="fa-solid fa-pen"></i></div>
						</label>
						<div class="field">
							<input type="text" autoComplete='off' ref={sUsername} required></input>
							<label>Username</label>
						</div>
						<div class="field">
							<input type="password" autoComplete='off' ref={sPassword1} required></input>
							<label>Password</label>
						</div>
						<div class="field">
							<input type="password" autoComplete='off' ref={sPassword2} required></input>
							<label>Confirm password</label>
							</div>
						<div class="field">
							<input type="submit" value="Signup"></input>
						</div>
						<div class="signup-link">
							Already have an account? <a onClick={rotate} id="LoginText">Login</a>
						</div>
					</form> */}
				</div>
			</div>
		</div>
	);
}
