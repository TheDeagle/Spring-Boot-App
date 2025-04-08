import Pfp from "../assets/Me.jpg";

export default function Message(props){
    return (
        <li class={`message ${props.direction} appeared`}>
            <img class="avatar" src={props.pfpPath ? props.pfpPath : Pfp}></img>
            <div class="text_wrapper">
                <div class="text">{props.msg}</div>
            </div>
        </li>
    );
}