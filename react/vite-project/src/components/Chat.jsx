import { useLocation, useNavigate, useParams } from 'react-router-dom';
import '../chat.css'
import Navbar from './Navbar';
import React, { useEffect, useRef, useState } from 'react';
import Pfp from "../assets/Me.jpg"
import { faL } from '@fortawesome/free-solid-svg-icons';
import initSocket from '../utils/initSocket';
import currentUser from '../utils/currentUser';
import Message from './Message';
import ReactDOM from 'react-dom/client';
import Card from './Card';


var _socket = new WebSocket("ws://localhost:8080/ws");

_socket.onopen = () => {
	console.log("A SOCKET CONNECTION HAS BEEN ESTABLISHED");
}

_socket.onclose = () => {
	console.log("A SOCKET CONNECTION HAS BEEN CLOSED");
}

var user = null;
var chatter = null;

export default function Chat(){
	var params = useParams();
	var navigator = useNavigate();
	var [loading, setLoading] = useState(true);
	var [_data, setData] = useState(null);
	var [_messages, setMessages] = useState(null);
	var _chatterUsername = params.username;
	var [_currentUserData, setUserData] = useState(null);
	var _messagesRef = useRef(null);
	
	useEffect(() => {
		var _msgs = null;
		_socket.onmessage = (e) => {
			var _li = document.createElement("div");
			var _msg = JSON.parse(e.data);
			var component = <Message direction="left" msg={_msg.message} pfpPath={user.pfpPath} ></Message>
			const root = ReactDOM.createRoot(_li);
			root.render(component);
			_messagesRef.current.appendChild(_li);
		}
		fetch("http://localhost:8080/api/users/me", {
            method: "GET",
            credentials: "include"
        })
        .then(Response => {
            if (!Response.ok)
                navigator("/login");
            return Response.json();
        })
        .then(Data => {
            user = Data;
			setUserData(user);
        })
		fetch("http://localhost:8080/api/users/" + _chatterUsername, {
			method: "GET",
			credentials: "include"
		})
		.then(Response => {
			if (!Response.ok)
				navigator("/");
			return Response.json();
		})
		.then(Data => {
			chatter = Data;
			setData(Data);
			setLoading(false);
		})
		fetch("http://localhost:8080/api/messages/" + _chatterUsername, {
			method: "GET",
			credentials: "include"
		})
		.then(Response => {
			return Response.json();
		})
		.then(Data => {
			var _msgs = Data.messages.map((obj) => {
				var _direction = obj.receiver == _chatterUsername ? "left" : "right";
				return <Message direction={_direction} msg={obj.message}></Message>;
			});
			setTimeout(() => {
				_messagesRef.current.scrollTop = _messagesRef.current.scrollHeight;
			}, 50);
			setMessages(_msgs);
		})
	}, [_chatterUsername, navigator]);

	var keyPress = (e) => {
		if (e.key == "Enter")
			msgFunc();
	}
	
	var _inputRef = useRef(null);
	var msgFunc = (e) => {
		if (_socket &&_currentUserData && _chatterUsername){
			if (_inputRef.current.value.trim() != "") {
				var _li = document.createElement("div");
				var component = <Message direction="right" msg={_inputRef.current.value} pfpPath={_currentUserData.pfpPath}></Message>
				const root = ReactDOM.createRoot(_li);
				root.render(component);
				_messagesRef.current.appendChild(_li);
				_socket.send(JSON.stringify(
					{
						receiver: _chatterUsername,
						message: _inputRef.current.value
					}
				));
				setTimeout(() => {
					_messagesRef.current.scrollTop = _messagesRef.current.scrollHeight;
				}, 50);
				_inputRef.current.value = "";
			}
		}
	}
	if (!loading && _currentUserData){
		return (
			<>
				<div class="chat_window">
					<Navbar></Navbar>
					<ul class="messages" ref={_messagesRef}>
						{_messages}
					</ul>
					<div class="bottom_wrapper clearfix">
						<div class="message_input_wrapper">
							<input class="message_input" ref={_inputRef} onKeyDown={keyPress} placeholder="Type your message here..." />
						</div>
						<div class="send_message" onClick={msgFunc}>
							<div class="icon"></div>
							<div class="text">Send</div>
						</div>
					</div>
				</div>
				<div class="message_template">
					<li class="message">
						<img class="avatar"></img>
						<div class="text_wrapper">
							<div class="text"></div>
						</div>
					</li>
				</div>
			</>
		);
	}
}
