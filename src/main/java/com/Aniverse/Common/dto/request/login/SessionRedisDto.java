package com.Aniverse.Common.dto.request.login;

public class SessionRedisDto {
	 private String sessionId;
	    private String userId;
	    private String accessToken;
	    private String refreshToken;
	    private Long accessExp;
	    private Long refreshExp;
	    private String loginType;   // LOCAL, KAKAO
	    private String clientType;  // WEB, APP, DESKTOP
	    private String ip;
	    private String userAgent;
	    private Long createdAt;
	    private Long lastAccessAt;

	    public String getSessionId() { return sessionId; }
	    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

	    public String getUserId() { return userId; }
	    public void setUserId(String userId) { this.userId = userId; }

	    public String getAccessToken() { return accessToken; }
	    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

	    public String getRefreshToken() { return refreshToken; }
	    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

	    public Long getAccessExp() { return accessExp; }
	    public void setAccessExp(Long accessExp) { this.accessExp = accessExp; }

	    public Long getRefreshExp() { return refreshExp; }
	    public void setRefreshExp(Long refreshExp) { this.refreshExp = refreshExp; }

	    public String getLoginType() { return loginType; }
	    public void setLoginType(String loginType) { this.loginType = loginType; }

	    public String getClientType() { return clientType; }
	    public void setClientType(String clientType) { this.clientType = clientType; }

	    public String getIp() { return ip; }
	    public void setIp(String ip) { this.ip = ip; }

	    public String getUserAgent() { return userAgent; }
	    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

	    public Long getCreatedAt() { return createdAt; }
	    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

	    public Long getLastAccessAt() { return lastAccessAt; }
	    public void setLastAccessAt(Long lastAccessAt) { this.lastAccessAt = lastAccessAt; }
}
