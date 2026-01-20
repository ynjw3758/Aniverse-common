package com.Aniverse.Common.entity;

import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.Aniverse.Common.dto.Taglist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@Document(collection = "upload")
public class Upload {
	
	private String id;
	private String text;
	private List<Map<String, Object>> file_info;
	private Map<String, Object> local_info;
	private String create;
	private String user;
	private int favorite;
	private List<Taglist> Tag_infos;
	
	public Upload() {}
	
	public void setLocal_info(Map<String, Object> local_info) {
		this.local_info = local_info;
	}
	
	public Map<String, Object> getLocal_info() {
		return local_info;
	}
	
	public List<Taglist> getTag_infos(){
		return Tag_infos;
	}
	
	public void setTag_infos(List<Taglist> Tag_infos) {
		this.Tag_infos = Tag_infos;
	}
	
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite =favorite;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user=user;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getCreate() {
		return create;
	}
	
	public void setCreate(String create) {
		this.create = create;
	}
	
	public List<Map<String, Object>> getFile_info(){
		return file_info;
	}
	
	public void setFile_info(List<Map<String, Object>> file_info) {
		this.file_info = file_info;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	

	
	 @Override
	    public String toString() {
	        return "file_info :" + file_info + " ,local_info:" + local_info + " ,text:" + text + "userid :" + user
	        		+ " ,create :" + create;
	    } 

}
