package com.spacetime.controller;

import java.util.Date;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacetime.config.auth.PrincipalDetails;
import com.spacetime.config.auth.ProjectService;
import com.spacetime.dto.ProjectResponseDto;
import com.spacetime.model.Project;
import com.spacetime.model.User;
import com.spacetime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RestApiController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ProjectService projectService;

	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}

	@PostMapping("token")
	public String token() {
		return "<h1>Token</h1>";
	}

	@PostMapping("join")
	protected void successfulAuthentication(@RequestBody User user, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		LocalDateTime now = LocalDateTime.now();
		// 현재 날짜/시간 출력
		System.out.println(now); // 2021-06-17T06:43:21.419878100
		// 포맷팅
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));

		String refreshToken = JWT.create().withSubject(formatedNow)
				.withExpiresAt(new Date(System.currentTimeMillis() + 300 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString()).sign(Algorithm.HMAC512("cos"));

		System.out.println("successfulAuthentication 실행됨 : 인증이 완료 되었다는 뜻임.");

		user.setUsername(user.getUsername());
		user.setNickname(user.getNickname());
		user.setRefreshToken(refreshToken);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_ADMIN");
		user.setStatus("Active");
		user.setRevisit(user.getRevisit() + 1);
		userRepository.save(user);

		// RSA방식은 아니고 Hash암호방식
		String jwtToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))).withClaim("id", user.getId())
				.withClaim("username", user.getUsername()).sign(Algorithm.HMAC512("cos"));

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", jwtToken);
		tokens.put("refresh_token", refreshToken);
		tokens.put("id", String.valueOf(user.getId()));
		tokens.put("username", user.getUsername());
		tokens.put("nickname", user.getNickname());
		tokens.put("revisit", String.valueOf(user.getRevisit()));
		tokens.put("role", "ROLE_ADMIN");
		tokens.put("status", "Active");
		tokens.put("JoinDate", String.valueOf(user.getCreatedTime()));
		tokens.put("ActiveTime", String.valueOf(user.getUpdateTime()));

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

	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader("Authorization");
		String refreshHeader = request.getHeader("refreshToken");

		System.out.println(authorizationHeader);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				
			
				
				

				String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(refreshToken).getClaim("username")
						.asString();


				User userEntity = userRepository.findByUsername(username);
				PrincipalDetails principalDetails = new PrincipalDetails(userEntity);


         System.out.println(userEntity);
         
         System.out.println("141번쨰 줄 " + refreshHeader);
         
         User user = new User();
         
       
         
         System.out.println("============== 토근 저장 성공 =======================");

          String reToken = principalDetails.getRefreshToken();
               System.out.println(reToken);

				
				long rToken = JWT.require(Algorithm.HMAC512("cos")).build().verify(refreshHeader)
						.getClaim("exp").asLong();
//	               
				System.out.println(rToken);

				long i = new Date().getTime() / (long) 1000;
				System.out.println(i);

				if (rToken > i) {

					String accessToken = JWT.create()
							.withSubject(Long.toHexString(principalDetails.getId() + new Date().getTime()))
							.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
							.withIssuer(request.getRequestURI().toString()).withClaim("id", principalDetails.getId())
							.withClaim("username", principalDetails.getUsername()).sign(Algorithm.HMAC512("cos"));
							


					System.out.println((userRepository.findById(principalDetails.getId())));

					Map<String, String> tokens = new HashMap<>();
					tokens.put("access_token", accessToken);
					tokens.put("refresh_token", principalDetails.getRefreshToken());
					tokens.put("id", String.valueOf(principalDetails.getId()));
					tokens.put("username", principalDetails.getUsername());
					tokens.put("nickname", principalDetails.getNickname());
					tokens.put("revisit", String.valueOf(principalDetails.getRevisit()));
					tokens.put("role", principalDetails.getRole());
					tokens.put("status", principalDetails.getStatus());
					tokens.put("JoinDate", String.valueOf(principalDetails.getCreateTime()));
					tokens.put("ActiveTime", String.valueOf(principalDetails.getUpdateTime()));
					

					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				} else {
					response.setHeader("error", "토큰이 만료 되었습니다.");
					response.setStatus(403, "권한이 없습니다.");
				}

			} catch (Exception e) {
				response.setHeader("error", e.getMessage());
				response.setStatus(403, "권한이 없습니다.");

				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}

		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}

//	@GetMapping("/user")
//	public List<UserResponceDto> refresh( String id, User user) {
//		userRepository.findById(Long.parseLong(id));
//	
//		return userService.findAll();
//	}

	// user, manager, admin권한만 접근 가능
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication: " + principal.getUsername());
		return "user";
	}

	// manager, admin만 접근 가능
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}

	// Admin 전용.
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}

	/**
	 * 게시글 생성
	 * 
	 * @throws Exception
	 */
	@PostMapping("/project")
	public Long save(@RequestBody final Project params) throws Exception {
		return projectService.save(params);
	}

	/**
	 * 게시글 리스트 조회
	 */
	@GetMapping("/projects")
	public List<ProjectResponseDto> findAll() {
		return projectService.findAll();
	}

	/**
	 * 게시글 수정
	 * 
	 * @throws Exception
	 */
	@PatchMapping("/projects/{id}")
	public Long save(@PathVariable final Long id, @RequestBody final Project params) throws Exception {
		return projectService.update(id, params);
	}

}
