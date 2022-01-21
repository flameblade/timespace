package com.spacetime.config.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacetime.config.auth.PrincipalDetails;
import com.spacetime.model.User;
import com.spacetime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter가 동작함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private final AuthenticationManager authenticationManager;

	

	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter: 로그인 시도중");

		// 1. username, password 받아서
		try {

//				BufferedReader br = request.getReader();
//				String input = null;
//				
//				while((input=br.readLine()) != null) {
//					System.out.println(input);
//				}
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getUsername(), user.getPassword());
					user.setRevisit(user.getRevisit());
//					user.(user);

			// PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이
			// 리턴됨.
			// DB에 있는 username과 password가 일치한다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);

			// ===> 로그인이 되었다는 뜻.
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUsername()); // 로그인 정상적으로 되었다는 뜻.
			// authentication 객체가 session 영역에 저장을 해야하고 그 방법을 return 해주면 됨..
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 거임.
			// 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session 넣어줍니다.
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 2. 정상인지 로그인 시도를 해보기. authenticationManager로 로그인 시도를 하면!!
		// PrincipalDetailsService가 실행됨.
		// PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.

		// 3. PrincipalDetails를 세션에 담고(권한 관리를 위해서)
		// 4. JWT 토큰을 만들어서 응답해주면 됨.
		return null;
	}


	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행되요.
	// JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해주면 됨.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		
		System.out.println("successfulAuthentication 실행됨 : 인증이 완료 되었다는 뜻임.");
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

		// RSA방식은 아니고 Hash암호방식
		String jwtToken = JWT.create().withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
				.withClaim("id", principalDetailis.getUser().getId())
				.withClaim("username", principalDetailis.getUser().getUsername()).sign(Algorithm.HMAC512("cos"));
		
		LocalDateTime now = LocalDateTime.now();
		// 현재 날짜/시간 출력
		System.out.println(now); // 2021-06-17T06:43:21.419878100
		// 포맷팅
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));

		 String refreshToken = JWT.create()
	                .withSubject(formatedNow)
	                .withExpiresAt(new Date((long)System.currentTimeMillis() + (long)1234567789))
	                .withIssuer(request.getRequestURI().toString())
	                .sign(Algorithm.HMAC512("cos"));
		 

	        Map<String, String> tokens = new HashMap<>();
	        tokens.put("access_token", jwtToken);
	        tokens.put("refresh_token", refreshToken);
	        tokens.put("username", principalDetailis.getUsername());
	        tokens.put("nickname", principalDetailis.getNickname());
	        tokens.put("revisit", String.valueOf(principalDetailis.getRevisit()));
	        tokens.put("status", principalDetailis.getStatus());
	        tokens.put("role", principalDetailis.getRole());
	        tokens.put("JoinDate", String.valueOf(principalDetailis.getCreateTime()));
	        tokens.put("ActiveTime",String.valueOf(principalDetailis.getUpdateTime()));

	        
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

//		@SuppressWarnings("deprecation")
//		Cookie cookie = new Cookie("Authorization", URLEncoder.encode("Bearer " + jwtToken));
//
//		// expires in 7 days
//		cookie.setMaxAge(7 * 24 * 60 * 60);
//
//		// optional properties
//		cookie.setSecure(true);
//		cookie.setHttpOnly(true);
//		cookie.setPath("/");
//
//		// add cookie to response
//		response.addCookie(cookie);
	}

}
