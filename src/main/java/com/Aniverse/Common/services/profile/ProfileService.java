package com.Aniverse.Common.services.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.entity.Upload;
import com.Aniverse.Common.mapper.ReactCommon.ReactCommon_Mapper;
import com.Aniverse.Common.mapper.UserInfo.Setting_Mapper;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.generation.Generation_Services;

@Service
public class ProfileService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper user_Mapper;
	private Setting_Mapper setting_mapper;
	private ReactCommon_Mapper ht_mapper;
    private MongoTemplate notiMongoTemplate;
    private MongoTemplate uploadMongoTemplate;
	private Generation_Services generation;
	private ReactCommon_Mapper react_common;
	
	public ProfileService(User_Mapper user_Mapper, /*User_Mapper mapper,*/
			ReactCommon_Mapper ht_mapper,Setting_Mapper setting_mapper,
			@Qualifier("notiMongoTemplate") MongoTemplate notiMongoTemplate,
			@Qualifier("uploadMongoTemplate") MongoTemplate uploadMongoTemplate, 
			Generation_Services generation,
			ReactCommon_Mapper react_common) {
		this.generation = generation;
		this.ht_mapper = ht_mapper;
		this.notiMongoTemplate = notiMongoTemplate;
		this.react_common = react_common;
		this.setting_mapper =setting_mapper;
		this.uploadMongoTemplate = uploadMongoTemplate;
		this.user_Mapper = user_Mapper;
	}
	
	
	public Map<String, Object> Profile(String id , String type , String Userid){
		Map<String, Object> response_data = new HashMap<String, Object>();
		Map<String, Object> result_data = new HashMap<String, Object>();
		Map<String, Object>token_data = new HashMap<String, Object>();
		List<Upload> files_info = new ArrayList<Upload>();
		List<Map<String, Object>> total_content = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> file_list = new ArrayList<Map<String, Object>>();
		List<Upload> content_list = new ArrayList<>();
		
		if(type.equals("My")) {
			logger.info("마이 페이지 조회");
			Map<String , Object> user_data = new HashMap<String,Object>();
			user_data = user_Mapper.user_info(id);
			logger.info("정보 조회 결과 :" + user_data);
            
            if(!user_data.containsKey("profile_img") && !user_data.containsKey("thumnail_img")) {
            	user_data.put("profile_img", "null");
            }
            logger.info("유저 정보 :" + user_data);
            files_info = uploadMongoTemplate.find(Query.query(Criteria.where("user").is(id)),Upload.class);
            logger.info("업로드한 파일 리스트 :" + files_info);

            
            for(int i=0;i<files_info.size();i++) {
                Map<String, Object> content_info= new HashMap<String, Object>();
            	String uuid="";
            	uuid = files_info.get(i).getId();
            	file_list = files_info.get(i).getFile_info();
            	List<Map<String, Object>> type_url = new ArrayList<Map<String, Object>>();
            	for(int j=0; j<file_list.size();j++) {
            		logger.info("파일 리스트 :" + file_list.get(j));
            		Map<String, Object> files = new HashMap<String, Object>();
					files.put("type", file_list.get(j).get("type").toString());
					String url=generation.generation_url(file_list.get(j).get("Path").toString());
					files.put("url", url);
					type_url.add(files);
					content_info.put("file_info", type_url);
					logger.info("content_infos: " + content_info);
            	}
            	content_info.put("content_id", uuid);
            	//TODO: 이 아래 부분은 다수정해야 한다.
            	content_list = uploadMongoTemplate.find(Query.query(Criteria.where("_id").is(uuid)), Upload.class);
            	
            	/*
				content = mongo_template.find(Query.query(Criteria.where("_id").is(uuid)), Content_info.class);
				logger.info("컨텐츠 정보 :" + content);
				for(int k=0; k<content.size();k++) {
					logger.info("컨텐츠 :" + content.get(k));
					String comment_id = content.get(k).getCommnet();
					String heart_id = content.get(k).getFavorite();
					comment =mongo_template.find(Query.query(Criteria.where("_id").is(comment_id)), Single_Comment.class); 
					heart = mongo_template.find(Query.query(Criteria.where("_id").is(heart_id)), Content_heart.class);
					if(comment.size()==0 && heart.size() == 0) {
						content_info.put("commnet", 0);
						content_info.put("heart", 0);
					}
					else if(comment.size()!=0 && heart.size() == 0) {
						content_info.put("commnet", comment.size());
						content_info.put("heart", 0);
					}
					
					else if(comment.size()==0 && heart.size() != 0) {
						content_info.put("commnet", 0);
						content_info.put("heart", heart.size());
					}
					else if(comment.size()!=0 && heart.size() != 0) {
						content_info.put("commnet", comment.size());
						content_info.put("heart", heart.size());
					}
				}	
				*/
				total_content.add(content_info);
            }
            

            response_data.put("id", user_data.get("id").toString());
            response_data.put("nickname", user_data.get("nickname").toString());
            response_data.put("profile_img", user_data.get("profile_img").toString());
            response_data.put("content_info", total_content);
            response_data.put("username", user_data.get("username").toString());
            response_data.put("exp", token_data.get("exp"));
            
            result_data.put("resultmsg", "login success");
            result_data.put("resultdata", response_data);
			result_data.put("resultcode", 200);
			
			return result_data;
		}
		else {
			boolean check_private = false;
			check_private = setting_mapper.search_private(Userid);
			
				Map<String , Object> user_data = new HashMap<String,Object>();
				user_data = user_Mapper.user_info(Userid);
				
				if(check_private == true) {
					logger.info("비공개 계정이다 팔로워해야 볼수 있다");
					Map<String, Object> Fl_info = new HashMap<String ,Object>(); //팔로워, 팔로잉, 팔로워 상태 체크
					Map<String, Object> userinfo = new HashMap<String ,Object>(); //아디디 저장
					userinfo.put("Myid", id);
					userinfo.put("Userid", Userid);
					Fl_info=user_Mapper.friendfind(userinfo);
		            if(!user_data.containsKey("profile_img") && !user_data.containsKey("thumnail_img")) {
		            	user_data.put("profile_img", "null");
		            }
		            response_data.put("id", user_data.get("id").toString());
		            response_data.put("nickname", user_data.get("nickname").toString());
		            response_data.put("profile_img", user_data.get("profile_img").toString());
		            response_data.put("username", user_data.get("username").toString());
		            response_data.put("private_check", "true");
		            response_data.put("Follower", Fl_info);
		            
		            result_data.put("resultmsg", "login success");
		            result_data.put("resultdata", response_data);
					result_data.put("resultcode", 200);
					
					return result_data;
					
				}
				else {
					logger.info("공개 계정");
					Map<String, Object> Fl_info = new HashMap<String ,Object>(); //팔로워, 팔로잉, 팔로워 상태 체크
					Map<String, Object> userinfo = new HashMap<String ,Object>(); //아디디 저장
					userinfo.put("Myid", id);
					userinfo.put("Userid", Userid);
					Fl_info=user_Mapper.friendfind(userinfo);
					Query query = new Query(Criteria.where("user").is(Userid))
						    .with(Sort.by(
						        Sort.Order.desc("favorite")
						    ))
						    .limit(10);
		            files_info = uploadMongoTemplate.find(query, Upload.class);
		            logger.info("list size :" + files_info.size());
		            for(int i=0;i<files_info.size();i++) {
		                Map<String, Object> content_info= new HashMap<String, Object>();
		            	String uuid="";
		            	uuid = files_info.get(i).getId();
		            	file_list = files_info.get(i).getFile_info();
		            	logger.info("리스트 :" + file_list);
		            	List<Map<String, Object>> type_url = new ArrayList<Map<String, Object>>();
		            	for(int j=0; j<file_list.size();j++) {
		            		logger.info("파일 리스트 :" + file_list.get(j));
		            		Map<String, Object> files = new HashMap<String, Object>();
							files.put("type", file_list.get(j).get("type").toString());
							String url=generation.generation_url(file_list.get(j).get("Path").toString());
							files.put("url", url);
							type_url.add(files);
							content_info.put("file_info", type_url);
							logger.info("content_infos: " + content_info);
		            	}
		            	content_info.put("content_id", uuid);
		            	//content_list =mongo_template.find(Query.query(Criteria.where("_id").is(uuid)), Upload.class);
		            	/*
						content = mongo_template.find(Query.query(Criteria.where("_id").is(uuid)), Content_info.class);
						logger.info("컨텐츠 정보 :" + content);
						for(int k=0; k<content.size();k++) {
							logger.info("컨텐츠 :" + content.get(k));
							String comment_id = content.get(k).getCommnet();
							String heart_id = content.get(k).getFavorite();
							comment =mongo_template.find(Query.query(Criteria.where("_id").is(comment_id)), Single_Comment.class); 
							heart = mongo_template.find(Query.query(Criteria.where("_id").is(heart_id)), Content_heart.class);
							if(comment.size()==0 && heart.size() == 0) {
								content_info.put("commnet", 0);
								content_info.put("heart", 0);
							}
							else if(comment.size()!=0 && heart.size() == 0) {
								content_info.put("commnet", comment.size());
								content_info.put("heart", 0);
							}
							
							else if(comment.size()==0 && heart.size() != 0) {
								content_info.put("commnet", 0);
								content_info.put("heart", heart.size());
							}
							else if(comment.size()!=0 && heart.size() != 0) {
								content_info.put("commnet", comment.size());
								content_info.put("heart", heart.size());
							}
						}	
						*/
						total_content.add(content_info);
		            }
		            
		            if(!user_data.containsKey("profile_img") && !user_data.containsKey("thumnail_img")) {
		            	user_data.put("profile_img", "null");
		            }
		            response_data.put("id", user_data.get("id").toString());
		            response_data.put("nickname", user_data.get("nickname").toString());
		            response_data.put("profile_img", user_data.get("profile_img").toString());
		            response_data.put("content_info", total_content);
		            response_data.put("username", user_data.get("username").toString());
		            response_data.put("private_check", "false");
		            response_data.put("Follower", Fl_info);
		            //response_data.put("exp", token_data.get("exp"));
		            
		            result_data.put("resultmsg", "login success");
		            result_data.put("resultdata", response_data);
					result_data.put("resultcode", 200);
					
					//return result_data;
				}
			 
			
			
		}
/*
		else {
			String check_refresh = "";
			check_refresh = redis.check_refresh(id);
			if(check_refresh == null) {
				logger.error("id로 조회한 refresh 토큰이 존재하지 않습니다 모두 재발급 필요");
				Map<String , Object> user_data = new HashMap<String,Object>();
				user_data = mapper.user_info(id);
				logger.info("정보 조회 결과 :" + user_data);
				if(user_data!= null) {
					logger.info("사용자 정보 조회 정상 모든 토큰 재발급");
					Map<String, Object> re_token = new HashMap<String, Object>();
					re_token = token.CreateToken(user_data.get("id").toString());

	                response_data.put("id", user_data.get("id").toString());
	                response_data.put("email", user_data.get("email").toString());
	                response_data.put("nickname", user_data.get("nickname").toString());
	                //response_data.put("gender", user_data.get("gender").toString());
	                response_data.put("profile_img", user_data.get("profile_img").toString());
	                response_data.put("thumbnail_img", user_data.get("thumnail_img").toString());
	                response_data.put("content_info", total_content);
	                response_data.put("exp", re_token.get("exp"));
	                response_data.put("username", user_data.get("username").toString());
	                response_data.put("access_token", re_token.get("access_token").toString());
	                logger.info("데이터 : " + response_data);
	                
					result_data.put("resultdata", response_data);
					result_data.put("resultmsg", "success");
					result_data.put("resultcode", 201);
					return result_data;
				}
				*/
				/*
				else {
					logger.error("정보를 알수 없는 사람입니다 접근 금지");
					result_data.put("resultdata", "null");
					result_data.put("resultmsg", "false");
					result_data.put("resultcode", 401);
					return result_data;
				}
			}
			*/
			/*
			else {
				logger.info("엑세스 토큰 재발급");
				Map<String, Object> user_info  =new HashMap<String, Object>();
				Map<String, Object> re_access_toekn  =new HashMap<String, Object>();
				user_info = userinfo.user_info(id);
				String re_token="";
				re_token = token.on_access(id);
				logger.info("사용자 정보 조회 : " + user_info);
				
	            if(!user_info.containsKey("profile_img") && !user_info.containsKey("thumnail_img")) {
	            	user_info.put("profile_img", "null");
	            }
				
				re_access_toekn = token.kakao_validtoken(re_token);
				
				logger.info("토큰 값 조화ㅣ : " + re_access_toekn);
                //response_data.put("uuid", user_info.get("connectid").toString());
                response_data.put("id", user_info.get("id").toString());
                response_data.put("email", user_info.get("email").toString());
                response_data.put("nickname", user_info.get("nickname").toString());
                //response_data.put("gender", user_info.get("gender").toString());
                response_data.put("profile_img", user_info.get("profile_img").toString());
                //response_data.put("thumbnail_img", user_info.get("thumnail_img").toString());
                response_data.put("content_info", total_content);
                response_data.put("exp", re_access_toekn.get("exp"));
                response_data.put("access_token", re_token);
                response_data.put("username", user_info.get("username").toString());
                logger.info("데이터 : " + response_data);
                
				result_data.put("resultdata", response_data);
				result_data.put("resultmsg", "success");
				result_data.put("resultcode", 201);
				
				
			}
			
		}
*/
		
		
		
		return result_data;
	}
	
	public Map<String ,Object> Small_Profile(String myid , String Userid){
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object>total_data = new HashMap<String, Object>();
		Map<String, Object>input_data = new HashMap<String, Object>();
		Map<String, Object>saerch_data = new HashMap<String, Object>();
		List<Upload> files_info = new ArrayList<Upload>();
		List<Map<String, Object>> file_list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> total_content = new ArrayList<Map<String, Object>>();
		List<String> type_url = new ArrayList<String>();
		input_data.put("Myid", myid);
		input_data.put("Userid", Userid);
        try {
			saerch_data = user_Mapper.friendfind(input_data);
			if(!saerch_data.containsKey("profile")){
				logger.info("프로필 없다");
				saerch_data.put("profile" , "null");
			}
			Query query = new Query(Criteria.where("user").is(Userid))
				    .with(Sort.by(
				        Sort.Order.desc("favorite")
				    ))
				    .limit(3);
            files_info = uploadMongoTemplate.find(query, Upload.class);
            if(files_info.size()!= 0) {
            	 for(int i=0;i<files_info.size();i++) {
            		 Map<String, Object> content_info= new HashMap<String, Object>();
                 	file_list = files_info.get(i).getFile_info();
                	
                	for(int j=0; j<1;j++) {
                		Map<String, Object> files = new HashMap<String, Object>();
    					String url=generation.generation_url(file_list.get(j).get("Path").toString());
    					type_url.add(url);
    					content_info.put("file_info", type_url);
                	}
                	total_content.add(content_info);
                 	
                 }
                 total_data.put("url", type_url);
                 total_data.put("user_data", saerch_data);
            }
            else {
                total_data.put("url", "null");
                total_data.put("user_data",saerch_data);
            };

            data.put("code", 200);
            data.put("msg", "success");
            data.put("data", total_data);
        }catch(Exception e) {
        	logger.error("에러 발생 :" + e);
        }

		return data;
	}

}
