import { useEffect, useState } from 'react';
import '../profile.css'
import Input from './Input';
import Navbar from './Navbar'
import PasswordInputs from './PasswordInputs';
import { useNavigate } from 'react-router-dom';
import Pfp from "../assets/Me.jpg"


function Profile(){
	var shown = false;
	var [loading, setLoading] = useState(true);
	var [_data, setData] = useState(null);
	var navigator = useNavigate();
	useEffect(() => {
		fetch("http://localhost:8080/api/users/me", {
			method: "GET",
			credentials: "include"
		})
		.then(Response => {
			if (!Response.ok)
				navigator("/login")
			return Response.json();
		})
		.then(Data => {
			setLoading(false);
			setData(Data);
		})
	});
	if (!loading){
		return (
			<div>
				<Navbar></Navbar>
				<div class="profile-page">
					<div class="content">
					<div class="content__cover">
						<img class="content__avatar" src={_data.pfpPath ? _data.pfpPath : Pfp}></img>
						<div class="content__bull"><span></span><span></span><span></span><span></span><span></span>
						</div>
					</div>
					<div class="content__actions"><a href="#"></a></div>
					<div class="content__title">
						<h1 id="USERNAME">{_data.username}</h1>
					</div>
					<div class="content__description">
						<PasswordInputs></PasswordInputs>
					</div>
					<ul class="content__list">
						<li><span>65</span>Friends</li>
						<li><span>43</span>Photos</li>
						<li><span>21</span>Comments</li>
					</ul>
					<div class="content__button"><a class="button" href="#">
						<div class="button__border"></div>
						<div class="button__bg"></div>
						<p class="button__text">Show more</p></a></div>
					</div>
					<div class="bg">
						<div></div>
						</div><div>
					</div>
				</div>
			</div>);
	}
}

export default Profile