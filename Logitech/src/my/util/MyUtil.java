package my.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class MyUtil {

	public static String getCurrentURL(HttpServletRequest request) {
		String currentURL = request.getRequestURL().toString();  // URL 주소를 가져와 String 타입으로 만든다.
		
		String queryString = request.getQueryString();
		
		if(queryString != null) {
			currentURL += "?"+queryString;  // ? 를 제외하고 가져오기 때문에 ? 를 결합한다.
			// http://localhost:9090/MyMVC/member/memberList.up?currentShowPageNo=15&sizePerPage=5&searchType=name&searchWord=승의
		}
		
		String ctxPath = request.getContextPath();  // /MyMVC
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
				
		currentURL = currentURL.substring(beginIndex + 1);
		
		return currentURL;
	}// end of public static String getCurrentURL(HttpServletRequest request){}----------------------
	
	// --- 날짜 비교하기 --- //
	public static int dateCompareTo(HttpServletRequest request, String day) {
		int result = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date paraDay = format.parse(day);
			
			Date today = new Date();
			String strToday = format.format(today);
			Date cptToday = format.parse(strToday);
			
			result = cptToday.compareTo(paraDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
