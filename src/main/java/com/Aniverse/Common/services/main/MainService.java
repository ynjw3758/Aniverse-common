package com.Aniverse.Common.services.main;

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
public class MainService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper user_Mapper;
    private MongoTemplate notiMongoTemplate;
    private MongoTemplate uploadMongoTemplate;
    private ReactCommon_Mapper react_common;
    private ReactCommon_Mapper ht_mapper;
    private Generation_Services generation;
    
    public MainService(User_Mapper user_Mapper, 
			ReactCommon_Mapper ht_mapper,
			@Qualifier("notiMongoTemplate") MongoTemplate notiMongoTemplate,
			@Qualifier("uploadMongoTemplate") MongoTemplate uploadMongoTemplate, 
			Generation_Services generation,
			ReactCommon_Mapper react_common) {
		this.generation = generation;
		this.ht_mapper = ht_mapper;
		this.notiMongoTemplate = notiMongoTemplate;
		this.react_common = react_common;
		this.uploadMongoTemplate = uploadMongoTemplate;
		this.user_Mapper = user_Mapper;
    }
	public Map<String, Object> Mainpage_refresh(String id) {
		Map<String, Object>result_data = new HashMap<String, Object>();
		Map<String, Object>response_data = new HashMap<String, Object>();
		Map<String, Object>local_info = new HashMap<String, Object>();
		Map<String, Object> Noti_Infos = new HashMap<>();
		List<Map<String, Object>> total_content = new ArrayList<Map<String, Object>>();
		List<Map> NotiList = new ArrayList<>();
		List<Upload> public_content = new ArrayList<Upload>();
		Query query = new Query(Criteria.where("user").ne(id))
			    .with(Sort.by(
			        Sort.Order.desc("favorite"),
			        Sort.Order.desc("create")
			    ))
			    .limit(20);
		public_content = uploadMongoTemplate.find(query, Upload.class);
		
		if(public_content.size() == 0) {
			Map<String , Object> user_data = new HashMap<String,Object>();
			user_data = user_Mapper.user_info(id);
	        response_data.put("id", user_data.get("id").toString());
	        response_data.put("email", user_data.get("email").toString());
	        response_data.put("nickname", user_data.get("nickname").toString());
	        //response_data.put("gender", user_data.get("gender").toString());
	        response_data.put("profile_img", user_data.get("profile_img").toString());
	        response_data.put("content_info", "null");
	        
	        result_data.put("tmsg", "file is empty");
	        result_data.put("tdata", response_data);
			result_data.put("code", 201);
			return result_data;
		}
		
		else {
			List<Map<String, Object>> files_info = new ArrayList<Map<String, Object>>();
			Map<String, Object> like_list= new HashMap<>();
			Map<String , Object> user_data = new HashMap<>();
			int favorite=0 ,Comment_cnt=0;;
			String userid = "";
			String fix_id="";
			boolean islike = false;
			for(int i=0; i<public_content.size();i++) {
				Map<String, Object> content_info= new HashMap<>();
				List<Map<String, Object>> type_url = new ArrayList<Map<String, Object>>();
				favorite = public_content.get(i).getFavorite();
				String contentid=public_content.get(i).getId();
				boolean iscnt=react_common.Comment_exist(contentid);
				if(iscnt) {
					Comment_cnt = react_common.comment_cnt(contentid);
					
				}
				else {
					Comment_cnt = 0;
				}
				userid =public_content.get(i).getUser(); 
				fix_id = public_content.get(i).getId();
				files_info = public_content.get(i).getFile_info();
				for(int j= 0; j<files_info.size(); j++) {
					Map<String, Object> files = new HashMap<String, Object>();
					files.put("type", files_info.get(j).get("type").toString());
					String url=generation.generation_url(files_info.get(j).get("Path").toString());
					files.put("url", url);
					type_url.add(files);
					content_info.put("file_info", type_url);
				}
				//TODO:PSQL에서 댓글 및 좋아요 유저 정보 가져오기
				content_info.put("content_id", fix_id);
				content_info.put("favorite", favorite);
				
				like_list.put("content_id", fix_id);
				like_list.put("user_id", id);
				islike =ht_mapper.isHeart_Check(like_list);
				if(islike == true) {
					content_info.put("like", "O");
				}
				else {
					content_info.put("like", "N");
				}
				
				user_data = user_Mapper.user_info(userid);
				content_info.put("Content_Comment", public_content.get(i).getText().toString());
	            content_info.put("nickname", user_data.get("nickname").toString());
	            content_info.put("user",userid);
	            content_info.put("comment_cnt", Comment_cnt);
	            if(!user_data.containsKey("profile_img")) {
	            	content_info.put("profile", "null");
	            }
	            else {
	            	content_info.put("profile", user_data.get("profile_img").toString());
	            }
	            local_info = (Map<String, Object>) public_content.get(i).getLocal_info();
	            if(local_info !=null) content_info.put("local_content", local_info.get("content"));
	            content_info.put("CreateDate", public_content.get(i).getCreate());
				total_content.add(content_info);

			}
			user_data = user_Mapper.user_info(id);
			Query NotiQuery = new Query(
				    new Criteria().andOperator(
				        Criteria.where("UserId").is(id),
				        Criteria.where("IsRead").is(false)
				    )
				);
			NotiQuery.fields().exclude("_id");
			NotiList =notiMongoTemplate.find(NotiQuery, Map.class, "Chat");
			
			logger.info("채팅 리스트 :" + NotiList);
			if(NotiList.size() != 0) Noti_Infos.put("Chat", NotiList);
			else Noti_Infos.put("Chat", NotiList);
	        response_data.put("id", user_data.get("id").toString());
	        response_data.put("email", user_data.get("email").toString());
	        response_data.put("nickname", user_data.get("nickname").toString());
	        //response_data.put("gender", user_data.get("gender").toString());
	        response_data.put("profile_img", user_data.get("profile_img").toString());
	        response_data.put("content_info", total_content);
	        response_data.put("Noti", Noti_Infos);
	        
	        result_data.put("msg", "login success");
	        result_data.put("data", response_data);
			result_data.put("code", 200);
		}		
		return result_data;
		
	}

}
