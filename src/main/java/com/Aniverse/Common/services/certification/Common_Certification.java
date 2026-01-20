package com.Aniverse.Common.services.certification;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.dto.Response;
import com.Aniverse.Common.dto.request.certification.SendMail;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.redis.Redis_Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Common_Certification {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final SecureRandom random = new SecureRandom();
	private JavaMailSender mailSender;
	private Redis_Services Redis;
	private User_Mapper userinfo;
	private String access_Key;
	private String secret_Key;
	
	public Common_Certification(JavaMailSender mailSender,
			                      Redis_Services Redis ,User_Mapper userinfo) {
		this.mailSender = mailSender;
		this.Redis = Redis;
		this.userinfo = userinfo;
	}
	
    private String AppbuildResetPasswordEmail(String otp) {
        return  "<div style='padding:20px; font-family:Arial, sans-serif; background-color:#f9f9f9;'>"
                + "<div style='max-width:500px; margin:0 auto; padding:20px; background:white; border-radius:10px; "
                + "box-shadow:0 2px 10px rgba(0,0,0,0.1); text-align:center;'>"
                
                + "<h2 style='color:#333;'>비밀번호 초기화 요청</h2>"
                + "<p style='font-size:16px;'>아래 인증번호를 입력하여 비밀번호를 재설정하세요.</p>"

                // OTP 값 중앙 정렬 + 위아래 margin 추가
                + "<div style='font-size:24px; font-weight:bold; padding:15px; margin:20px 0; background:#f3f3f3; "
                + "border-radius:5px; display:inline-block;'>"
                + otp + "</div>"

                + "<p style='font-size:14px; color:#666;'>인증번호는 <strong style='color:red;'>3분간 유효</strong>합니다.</p>"

                // 안내 문구
                + "<p style='font-size:12px; color:#999; margin-top:15px;'>본인이 요청하지 않았다면 이메일을 무시하세요.</p>"

                + "</div>"
            + "</div>";
    }
	
	
    private String buildResetPasswordEmail(String otp) {
        return  "<div style='padding:20px; font-family:Arial, sans-serif; background-color:#f9f9f9;'>"
                + "<div style='max-width:500px; margin:0 auto; padding:20px; background:white; border-radius:10px; "
                + "box-shadow:0 2px 10px rgba(0,0,0,0.1); text-align:center;'>"
                
                + "<h2 style='color:#333;'>비밀번호 초기화 요청</h2>"
                + "<p style='font-size:16px;'>아래 인증번호를 입력하여 비밀번호를 재설정하세요.</p>"

                // OTP 값 중앙 정렬 + 위아래 margin 추가
                + "<div style='font-size:24px; font-weight:bold; padding:15px; margin:20px 0; background:#f3f3f3; "
                + "border-radius:5px; display:inline-block;'>"
                + otp + "</div>"

                + "<p style='font-size:14px; color:#666;'>인증번호는 <strong style='color:red;'>3분간 유효</strong>합니다.</p>"

                // 안내 문구
                + "<p style='font-size:12px; color:#999; margin-top:15px;'>본인이 요청하지 않았다면 이메일을 무시하세요.</p>"

                + "</div>"
            + "</div>";
    }
    
    private String RandomGeneration() {
    	String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder otp = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
    	return otp.toString();
    }
    
    
    public Map<String, Object> verifiCertifi(String id, String code){
    	Map<String, Object> result = new HashMap<>();
    	result = Redis.Certifi_validate(id, code);
    	logger.info("레디스 인증번호 조회결과 :" + result);
    	
    	return result;
    }
    
	public Map<String, Object> sendEmail(SendMail info ,String platformType ){
		Map<String, Object> result= new HashMap<String, Object>();
		boolean isExist= false;
		 isExist = userinfo.exist_info(info);
		 
		 if(isExist) {
			 //TODO : 이메일 전송
		        MimeMessage message = mailSender.createMimeMessage();
		        try {
		        	if("app".equalsIgnoreCase(platformType)) {
		        		Map<String, Object> insertdata = new HashMap<>();
						MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	                    String opt =RandomGeneration(); 
				        helper.setTo(info.getEmail());
				        helper.setSubject("애완동물 이메일 인증 테스트");
				        helper.setText(AppbuildResetPasswordEmail(opt), true); // HTML로 전송
				        mailSender.send(message);
				        long endtime = 10*6*1000*3;
				        logger.info("이메일 끝 레디스 저장");
				        insertdata.put("Id", info.getId());
				        insertdata.put("Certification", opt);
				        Redis.Certifi_insert(insertdata);
		        	}else {
						MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
						Map<String, Object> insertdata = new HashMap<>();
	                    String opt =RandomGeneration(); 
				        helper.setTo(info.getEmail());
				        helper.setSubject("애완동물 이메일 인증 테스트");
				        helper.setText(buildResetPasswordEmail(opt), true); // HTML로 전송
				        mailSender.send(message);
				        long endtime = 10*6*1000;
				        logger.info("이메일 끝 레디스 저장");
				        insertdata.put("Id", info.getId());
				        insertdata.put("Certification", opt);
				        Redis.Certifi_insert(insertdata);
		        	}

			        
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("이메일 발송 실패");
					throw new CustomException(ErrorCode.FAIL_SEND_MAIL, null);
				}
			 result.put("code" , 200);
			 result.put("msg", "이메일 전송 완료");
		 }
		 else {
			 //TODO : 사용자에게 다시 입력 요청
			 throw new CustomException(ErrorCode.ISNOT_INFO, null);
			 
		 }
		
		
		return result;
	}
	
    //핸드폰 인증 
    public Map<String, Object> findpw(Map<String, Object> info){
    	logger.info("사용자 핸드폰인증번호 ");
    	Map<String, Object> result_info = new HashMap<String, Object>();
    	Map<String, Object> receive_info = new HashMap<String, Object>();
    	
    	try {
			receive_info = SendMassage(info);
			logger.info("인증번호 결과 :" + receive_info);
			result_info.put("resultcode" , 200);
			result_info.put("resulmsg" , "인증번호 발송");
			result_info.put("certifi_number", receive_info.get("number").toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block\
			logger.error("error : " + e.getClass()+  " , " + e.getMessage());
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("error : " + e.getClass()+  " , " + e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("error : " + e.getClass()+  " , " + e.getMessage());
			e.printStackTrace();
		}

    	
    	return result_info;
    	
    }
    
	//sns 비밀번호 인증
    //@Async
	public Map<String, Object> SendMassage(Map<String, Object> Signature)
			throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
	       Map<String, Object> result_data = new HashMap<String, Object>();
	        boolean phone_check = false;
	        logger.info("인증번호 요청 파라미터 : " + Signature);
	        phone_check=userinfo.Search_Phone(Signature.get("Phone_number").toString());
	        logger.info("인증핸드폰 번화 중복 확인 :" + phone_check);
	        if(phone_check == false) {
	        
	        logger.info("시그니처 요청값 : " + Signature);
			Response responses = new Response();
			
			String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + Signature.get("service_id").toString() + "/messages";
			String time = Long.toString(System.currentTimeMillis());
			Map<String, Object> signatures = new HashMap<String, Object>();
			signatures = Signature;
			signatures.put("url", apiUrl);
			signatures.put("time", time);
			String accecckey =Signature.get("access_key").toString(); 
			logger.info("signatures : " + signatures);
			// JSON 을 활용한 body data 생성
			
			JSONObject bodyJson = new JSONObject();
			JSONObject toJson = new JSONObject();
		    JSONArray  toArr = new JSONArray();
		    String randomnum = "{"+"애견 미디어 본인 인증 인증번호 : " + "{" + CreateNumber() + "}" + "}";
		    String only_number =randomnum.replaceAll("[^0-9]", ""); 
		    logger.info("숫자만 추출 :" + only_number);
		    
		    toJson.put("to",Signature.get("Phone_number"));						// Mandatory(필수), messages.to	수신번호, -를 제외한 숫자만 입력 가능
		    toArr.put(toJson);
		    bodyJson.put("type","SMS");							// Madantory, 메시지 Type (SMS | LMS | MMS), (소문자 가능)
		    bodyJson.put("contentType","COMM");					// Optional, 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
		    bodyJson.put("countryCode","82");					// Optional, 국가 전화번호, (default: 82)
		    bodyJson.put("from","01097977894");					// Mandatory, 발신번호, 사전 등록된 발신번호만 사용 가능								// Optional, 기본 메시지 제목, LMS, MMS에서만 사용 가능
		    bodyJson.put("content",randomnum);	// Mandatory(필수), 기본 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte
		    bodyJson.put("messages", toArr);	
		    String body = bodyJson.toString();
		    
		    logger.info("request_body : " + body);
		    String resultdata = "";
			  try {
			 URL url = new URL(apiUrl);

	         HttpURLConnection con = (HttpURLConnection)url.openConnection();
	         con.setUseCaches(false);
	         con.setDoOutput(true);
	         con.setDoInput(true);
	         con.setConnectTimeout(5000);
	         con.setReadTimeout(3000);
	         con.setRequestProperty("content-type", "application/json");
	         con.setRequestProperty("x-ncp-apigw-timestamp", time);
	         con.setRequestProperty("x-ncp-iam-access-key", accecckey);
	         con.setRequestProperty("x-ncp-apigw-signature-v2", MakeSignature(signatures));
	         con.setRequestMethod("POST");
	         con.setDoOutput(true);
	         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	         
	         wr.write(body.getBytes());
	         wr.flush();
	         wr.close();

	         int responseCode = con.getResponseCode();
	         BufferedReader br;
	         logger.info("responseCode" +" " + responseCode);
	         
	         if(responseCode == 202) { // 정상 호출
	             br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	             Map<String, Object> save_data = new HashMap<String, Object>();
	             save_data.put("phonnumber",Signature.get("number").toString());
	             long timeout = 0;
	             timeout = 1000 * 60 * 3;
	             save_data.put("certifi_number", only_number);
	             Redis.setData(save_data.get("phonnumber").toString(),save_data.get("certifi_number").toString(),timeout);
	             result_data.put("resultcode", 200);
	             result_data.put("resultmsg", "본인 인증완료");
	             result_data.put("number", only_number);
	             
	         } else { // 에러 발생
	        	 responses.setResultcode(responseCode);
	        	 responses.setVercode(null);
	        	 responses.setResultMsg("본인 인증 오류 발생");
	             br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	             logger.error("response_error : " + br.toString());
	             result_data.put("resultcode", responseCode);
	             result_data.put("resultmsg", "본인 인증 실패");
	             result_data.put("number", 0);
	         }

	         String inputLine;
	         StringBuffer response = new StringBuffer();
	         while ((inputLine = br.readLine()) != null) {
	             response.append(inputLine);
	         }
	         br.close();
	         resultdata = response.toString();
	         responses.setResultMsg(resultdata);
	         logger.info("response_data : " + response.toString());

	     } catch (Exception e) {
	    	 
	         logger.error("error_data : " + e.getMessage());
	     }
	        }
	        else if(phone_check == true && !Signature.containsKey("Reset")){
	        	logger.info("중복 핸드폰 번호입니다");
	            result_data.put("resultcode", 400);
	            result_data.put("resultmsg", "핸드폰 번호 중복");
	            result_data.put("number", 0);
	        }
	        
	        else if(phone_check == true && Signature.containsKey("Reset")) {
	        	logger.info("비밀번호 리셋을 위한 핸드폰 인증번호 전송");
				Response responses = new Response();
				
				String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + Signature.get("service_id").toString() + "/messages";
				String time = Long.toString(System.currentTimeMillis());
				Map<String, Object> signatures = new HashMap<String, Object>();
				signatures = Signature;
				signatures.put("url", apiUrl);
				signatures.put("time", time);
				String accecckey =Signature.get("access_key").toString(); 
				logger.info("signatures : " + signatures);
				// JSON 을 활용한 body data 생성
				
				JSONObject bodyJson = new JSONObject();
				JSONObject toJson = new JSONObject();
			    JSONArray  toArr = new JSONArray();
			    String randomnum = "{"+"애견 미디어 본인 인증 인증번호 : " + "{" + CreateNumber() + "}" + "}";
			    String only_number =randomnum.replaceAll("[^0-9]", ""); 
			    logger.info("숫자만 추출 :" + only_number);
			    
			    toJson.put("to",Signature.get("Phone_number"));						// Mandatory(필수), messages.to	수신번호, -를 제외한 숫자만 입력 가능
			    toArr.put(toJson);
			    bodyJson.put("type","SMS");							// Madantory, 메시지 Type (SMS | LMS | MMS), (소문자 가능)
			    bodyJson.put("contentType","COMM");					// Optional, 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
			    bodyJson.put("countryCode","82");					// Optional, 국가 전화번호, (default: 82)
			    bodyJson.put("from","01097977894");					// Mandatory, 발신번호, 사전 등록된 발신번호만 사용 가능								// Optional, 기본 메시지 제목, LMS, MMS에서만 사용 가능
			    bodyJson.put("content",randomnum);	// Mandatory(필수), 기본 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte
			    bodyJson.put("messages", toArr);	
			    String body = bodyJson.toString();
			    
			    logger.info("request_body : " + body);
			    String resultdata = "";
				  try {
				 URL url = new URL(apiUrl);

		         HttpURLConnection con = (HttpURLConnection)url.openConnection();
		         con.setUseCaches(false);
		         con.setDoOutput(true);
		         con.setDoInput(true);
		         con.setConnectTimeout(5000);
		         con.setReadTimeout(3000);
		         con.setRequestProperty("content-type", "application/json");
		         con.setRequestProperty("x-ncp-apigw-timestamp", time);
		         con.setRequestProperty("x-ncp-iam-access-key", accecckey);
		         con.setRequestProperty("x-ncp-apigw-signature-v2", MakeSignature(signatures));
		         con.setRequestMethod("POST");
		         con.setDoOutput(true);
		         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		         
		         wr.write(body.getBytes());
		         wr.flush();
		         wr.close();

		         int responseCode = con.getResponseCode();
		         BufferedReader br;
		         logger.info("responseCode" +" " + responseCode);
		         
		         if(responseCode == 202) { // 정상 호출
		        	 logger.info("정상");
		             br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		             Map<String, Object> save_data = new HashMap<String, Object>();
		             save_data.put("phonnumber",Signature.get("Phone_number").toString());
		             long timeout = 0;
		             timeout = 1000 * 60 * 3;
		             save_data.put("certifi_number", only_number);
		             Redis.setData(save_data.get("phonnumber").toString(),save_data.get("certifi_number").toString(),timeout);
		             result_data.put("resultcode", 200);
		             result_data.put("resultmsg", "본인 인증완료");
		             result_data.put("number", only_number);
		             
		         } else { // 에러 발생
		        	 logger.info("실패");
		        	 responses.setResultcode(responseCode);
		        	 responses.setVercode(null);
		        	 responses.setResultMsg("본인 인증 오류 발생");
		             br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		             logger.error("response_error : " + br.toString());
		             result_data.put("resultcode", responseCode);
		             result_data.put("resultmsg", "본인 인증 실패");
		             result_data.put("number", 0);
		         }

		         String inputLine;
		         StringBuffer response = new StringBuffer();
		         while ((inputLine = br.readLine()) != null) {
		             response.append(inputLine);
		         }
		         br.close();
		         resultdata = response.toString();
		         responses.setResultMsg(resultdata);
		         logger.info("response_data : " + response.toString());

		     } catch (Exception e) {
		    	 
		         logger.error("error_data : " + e.getMessage());
		     }
	        }
	     logger.info("resultcode : " + result_data);
			return result_data;
		}
	
	// 전달할 데이터를 암호화 해주는 작업
	public String MakeSignature(Map<String, Object> certification_data)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		logger.info("본인 인증 서비스 시작");
		logger.info("data : " + certification_data);
		String time = certification_data.get("time").toString();
		String space = " ";
		String newLine = "\n";
		String method = "POST";
		String url = "/sms/v2/services/"+certification_data.get("service_id").toString() +"/messages";
		access_Key = certification_data.get("access_key").toString();
		secret_Key = certification_data.get("secret_key").toString();
		String message = new StringBuilder().append(method).append(space).append(url).append(newLine).append(time)
				.append(newLine).append(access_Key).toString();
		logger.info("makesignature message : " + message);

		SecretKeySpec signingKey = new SecretKeySpec(secret_Key.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
		logger.info("암호화 데이터  : " + encodeBase64String);

		return encodeBase64String;

	}
	
	public String CreateNumber() {
		Random random = new Random();

		int createnumber = 0;
		String toString = null;
		int letter = 8;
		String resultnumber = "";
		for (int i = 0; i < letter; i++) {
			createnumber = random.nextInt(9);
			toString = Integer.toString(createnumber);
			resultnumber += toString;
		}

		return resultnumber;
	}

}
