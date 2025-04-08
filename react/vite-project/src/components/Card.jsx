import { useNavigate } from 'react-router-dom';
import Pfp from '../assets/Me.jpg'

export default function Card(props){
	var navigator = useNavigate();
	var startMessaging = () => {
		navigator("/chat/" + props.username);
	}
	return(
		<div class="profile-container">
			<img src={props.pfp ? props.pfp : Pfp} className='profile-picture'></img>
			<div class="profile-info">
				<h2>{props.username}</h2>
				<button class="btn btn-outline-primary" onClick={startMessaging}>Message</button>
			</div>
		</div>
	);
}

