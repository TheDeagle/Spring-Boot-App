import Navbar from "./Navbar"
import Card from "./Card";
import '../index.css'
import Search from "./Search";
import { data, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";


var _socket  = new WebSocket("ws://localhost:8080/ws");

_socket.onmessage = () => {
	console.log('A SOCKET CONNECTION HAS BEEN ESTABLISHED');
}

_socket.onclose = () => {
	console.log("A SOCKET CONNECTION HAS BEEN CLOSED");
}

export default function Home(){
	var _data = null;
	var nav = useNavigate();
	var [_data, setData] = useState(null);
	var [loading, setLoading] = useState(true);
	var [_currentUserData, setUserData] = useState(null);
	useEffect(() => {
		var user = null;
		fetch("http://localhost:8080/api/users/me", {
            method: "GET",
            credentials: "include"
        })
        .then(Response => {
            if (!Response.ok)
                nav("/login");
            return Response.json();
        })
        .then(Data => {
            user = Data;
			setUserData(user);
        })
		fetch("http://localhost:8080/api/users/all", {
			method: "GET",
			credentials: "include"
		})
		.then(Response => {
			if (!Response.ok)
				nav("/login");
			return Response.json();
		})
		.then(Data => {
			if (user) {
				var elements = Data.map((obj) => {
					if (obj.username != user.username)
						return <Card username={obj.username} pfp={obj.pfpPath}></Card>;
				});
				setData(elements);
				setLoading(false);
			}
		})
	}, [nav]);
	if (!loading && _currentUserData) {
		return (
			<div>
				<Navbar></Navbar>
				<div className="Section">
					<div className="CardsWrapper">
						{_data}
					</div>
				</div>
			</div>);
	}
}

