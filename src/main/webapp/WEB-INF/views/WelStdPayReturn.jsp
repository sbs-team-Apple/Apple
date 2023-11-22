<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="kr.co.paywelcome.util.ParseUtil"%>
<%@ page import="kr.co.paywelcome.util.SignatureUtil" %>
<%@ page import="kr.co.paywelcome.util.HttpUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style type="text/css">
		body { background-color: #efefef;}
		body, tr, td {font-size:11pt; font-family:굴림,verdana; color:#433F37; line-height:19px;}
		/*table, img {border:none}*/
		
	</style>
	<script language="javascript" type="text/javascript">
		function Submit_me(){
			payForm.target="INIpayStd_Return";
			payForm.submit();
			self.close();
		}
	</script>
</head>
<body bgcolor="#FFFFFF" text="#242424" leftmargin=0 topmargin=15 marginwidth=0 marginheight=0 bottommargin=0 rightmargin=0>
	<div style="padding:10px;width:100%;font-size:14px;color: #ffffff;background-color: #000000;text-align: center">
		웰컴페이먼츠 표준결제 인증결과 수신 / 승인요청 표시 샘플
	</div>
<% 
	//#############################
	// 인증결과 파라미터 일괄 수신
	//#############################
	request.setCharacterEncoding("UTF-8");
	Map<String,String> paramMap = new Hashtable<String,String>();
	Enumeration elems = request.getParameterNames();
	String temp = "";

	while(elems.hasMoreElements())
	{
		temp = (String) elems.nextElement();
		paramMap.put(temp, request.getParameter(temp));
	}

	System.out.println("paramMap : "+ paramMap.toString());

	out.println("####인증결과/승인요청####");
	out.println("<br/>");

	//############################################
	// 전문 필드 값 설정(***가맹점 개발수정***)
	//############################################

	String mid 		= paramMap.get("mid");						// 가맹점 ID 수신 받은 데이터로 설정
	String signKey	= "QjZXWDZDRmxYUXJPYnMvelEvSjJ5QT09";		// 가맹점에 제공된 키(이니라이트키) (가맹점 수정후 고정) !!!절대!! 전문 데이터로 설정금지
	String timestamp= SignatureUtil.getTimestamp();				// util에 의해서 자동생성
	String charset 	= "EUC-KR";								    // 리턴형식[UTF-8,EUC-KR](가맹점 수정후 고정)
	String format 	= "JSON";								    // 리턴형식[XML,JSON,NVP](가맹점 수정후 고정)
	String authToken= paramMap.get("authToken");			    // 취소 요청 tid에 따라서 유동적(가맹점 수정후 고정)
	String authUrl	= paramMap.get("authUrl");				    // 승인요청 API url(수신 받은 값으로 설정, 임의 세팅 금지)
	String netCancelUrl= paramMap.get("netCancelUrl");			// 망취소 API url(수신 받은 값으로 설정, 임의 세팅 금지)
	String ackUrl 	= paramMap.get("checkAckUrl");			    // 가맹점 내부 로직 처리후 최종 확인 API URL(수신 받은 값으로 설정, 임의 세팅 금지)
	String merchantData = URLDecoder.decode(paramMap.get("merchantData"),"UTF-8");	// 가맹점 임의 데이터 (한글 입력 시 URL decoding 하여 원본 문자 확인, 결제 요청 시 전달한 값 그대로 반환됩니다.)
	String price = "";  // 가맹점에서 최종 결제 가격 표기 (필수입력아님)

	// ##################
	// 인증결과 표시
	// ##################
	System.out.println("##승인요청 API 요청##");

	String returnForm = "<인증결과 내역><br/><br/><table border='1'><tr style='background-color:#bbb'><td>결과코드</td><td>결과메세지</td></tr>";
	returnForm += "<tr><td>resultCode</td><td>" + paramMap.get("resultCode") + "</td></tr>";
	returnForm += "<tr><td>resultMsg</td><td>" + paramMap.get("resultMsg") + "</td></tr>";
	returnForm += "<tr><td>mid</td><td>" + mid + "</td></tr>";
	returnForm += "<tr><td>orderNumber</td><td>" + paramMap.get("orderNumber") + "</td></tr>";
	returnForm += "<tr><td>authUrl</td><td>" + paramMap.get("authUrl") + "</td></tr>";
	returnForm += "<tr><td>merchantData</td><td>" + merchantData + "</td></tr>";
	returnForm += "</table>";

	out.println(returnForm);
	out.flush();
%>
	<%--#####################--%>
	<%-- 인증이 성공일 경우만--%>
	<%--#####################--%>
	<% if("0000".equals(paramMap.get("resultCode"))){ %>
	<br>
	############################################ <br>
		--------------<br>
		호출  URL : authUrl<br>
		승인 요청 시 필수 파라미터 : mid, authToken, signature, timestamp<br>
		선택 파라미터 : charset(default UTF-8), format(default XML)<br>
		--------------<br>
	 #############################################<br>

	 인증에 <strong>성공</strong>하였습니다. <br>
	 아래 버튼을 클릭해 <strong>승인요청</strong>까지 진행해야만 승인완료 후 <strong>실결제</strong>처리됩니다.<br><p></p>
	 
	<form id="payForm" method="post" action="WelStdPayResult.jsp">
		<input type="hidden" name="mid" value="<%=mid %>" />
		<input type="hidden" name="authToken" value="<%=authToken %>" />
		<input type="hidden" name="authUrl" value="<%=authUrl%>" />
		<%-- 가격 위변조 체크 기능(선택사용)--%>
		<%-- <input type="hidden" name="price" value="<%=price%>" />--%>
		<%-- 망취소시 사용되는 url--%>
		<input type="hidden" name="netCancelUrl" value="<%=netCancelUrl%>" />
		<input id="payReq" type="button" value="승인 요청하기" onclick="Submit_me();">
	</form>
	<%} else { %>
	인증에 실패하였습니다. <br>
	인증실패 사유 : <%=paramMap.get("resultMsg") %>
	<%}%>


