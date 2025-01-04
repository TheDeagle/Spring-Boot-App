export function getCookie(cookieName){
	var Cookies = document.cookie.split(";");
	for (let Cookie of Cookies){
		const [cookieKey, cookieValue] = Cookie.split("=");
		if (cookieKey == cookieName)
			return cookieValue;
	}
	return "";
}