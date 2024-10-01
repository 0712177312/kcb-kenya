package com.compulynx.compas.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

import static java.util.Collections.emptyList;

class TokenAuthenticationService {
	//static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final long EXPIRATIONTIME = 8_640_000_000L; // 100 days
	static final String SECRET = "KADJAHDFKAEYAGHKSDYFAEYGSFDBAHSDHFYEAHFYAEJFSLYEHFAGHSYEFAHSBDFHAKYESFSAHFDGAESKYSGHFGAKFGHAKFAFDHGAKFSGHASGHFGAHhkaghfkafgafkaghfdgkfagfhagfhdsf32323347273727adkafhdfakfgKAGFAKDFGA";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	static final String HEADER_CUSTOM_HEADER = "Access-Control-Expose-Headers";

	static void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);


		System.out.println("JWT");
		System.out.println(JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token=request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();

			return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
		}
		return null;
	}


	public static void main(String[] args) {
		String JWT = Jwts.builder().setSubject("admin")
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		System.out.println("JWT");
		System.out.println(JWT);


	}
}
