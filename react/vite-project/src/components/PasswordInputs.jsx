import { useRef, useState } from "react";
import Input from "./Input"

export default function PassowrdInputs({visible}){
	const style = {
		display: "flex",
		flexDirection: "column",
		gap: "15px",
		marginTop: "2.5rem",
		textAlign: "center",
		order: "2",
	}
	const [state, setState] = useState(false);
	const inputRef = useRef(null);
	const visibilty = () => {
		setState(state => !state);
	}
	return(
		<div style={style}>
			<Input foo={visibilty} visible={state} icon={true} inputRef={inputRef} holder={"Old password"}></Input>
			<Input visible={state} icon={false} holder={"New password"}></Input>
			<Input visible={state} icon={false} holder={"Confirm password"}></Input>
		</div>
	);
}