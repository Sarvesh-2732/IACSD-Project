package com.swasth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class GoogleTokenRequest {
    private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public GoogleTokenRequest(String token) {
		super();
		this.token = token;
	}

	public GoogleTokenRequest() {
		super();
	}
    
}