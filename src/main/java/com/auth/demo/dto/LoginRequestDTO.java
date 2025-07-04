package com.auth.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String grantType;          // "password" or "refreshToken"
    private String username;
    private String password;
    private boolean withRefreshToken;
    private String refreshToken;
	public LoginRequestDTO() {
		super();
	}
	public LoginRequestDTO(String grantType, String username, String password, boolean withRefreshToken,
			String refreshToken) {
		super();
		this.grantType = grantType;
		this.username = username;
		this.password = password;
		this.withRefreshToken = withRefreshToken;
		this.refreshToken = refreshToken;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isWithRefreshToken() {
		return withRefreshToken;
	}
	public void setWithRefreshToken(boolean withRefreshToken) {
		this.withRefreshToken = withRefreshToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
    
    
}
