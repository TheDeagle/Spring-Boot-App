import { useParams } from "react-router-dom";
import Message from "../components/Message";

var _socket = null;

export default function initSocket(url, ref){
    if (_socket)
        _socket.close();
    _socket = new WebSocket("http://localhost:8080/ws");
    
    _socket.onopen = () => {
        console.log("A SOCKET CONNECTION HAS BEEN ESTABLSHED!");
    }

    if (url.pathname.includes("/chat")){
        _socket.onmessage = (e) => {
            console.log("A MESSAGE HAS BEEN RECEIVED FROM THE SOCKET: " + e.data);
            var msg = JSON.parse(e.data);
            var component = <Message msg={msg.nessage} direction="left" ></Message>
        }
    }

    _socket.onclose = () => {
        console.log("A SOCKET CONNECTION HAS BEEN CLOSED!");
    }
    return _socket;    
}