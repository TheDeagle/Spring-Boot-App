import '../input.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { useRef } from 'react';

export default function Input({foo, inputRef, visible, icon, holder}){
	var placeholder = null;
	if (icon)
		placeholder = <FontAwesomeIcon onClick={foo} id="eye" icon={faEye} />
	
	return (
		<label class="search-label">
			<input ref={inputRef} type={visible ? "text" : "password"} name="text" class="input" required="true" placeholder={holder}></input>
			{placeholder}
		</label>
	);
}