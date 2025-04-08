import { data, useNavigate } from "react-router-dom";

var _currentUserData = null;

export default async function currentUser(navigator){
    if (!_currentUserData){
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
            window.sessionStorage.setItem("user", _currentUserData);
            _currentUserData = Data;
        })
        return _currentUserData;
    }
}