package com.example.tiktok.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiktok.entity.Account;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtUtil {
    // lớp này dùng để ký (là hoạt động mã hóa tạo ra chữ ký) và xác nhận (verify) token
    public static Algorithm algorithm;
// giữ phương thức xác minh đúng định dạng  JWT và đúng chữ ký
    private static JWTVerifier verifier;
    public static String JWT_SECRET_KEY = "secret-changed";
    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = ONE_SECOND * 60;
    public static final int ONE_HOUR = ONE_MINUTE * 60;
    public static final int ONE_DAY = ONE_HOUR * 24;
    public static final String ROLE_CLAIM_KEY = "role";
    private static final String DEFAULT_ISSUER = "T2009M1";

    public static Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(JWT_SECRET_KEY.getBytes());
        }
        return algorithm;
    }

    static int time =  ONE_DAY * 3;

    public static JWTVerifier getVerifier() {
        if(verifier == null) {
            verifier = JWT.require(getAlgorithm()).build();
        }
        return verifier;
    }

    public static DecodedJWT getDecodedJwt(String token){
        DecodedJWT decodedJWT = getVerifier().verify(token);
        return decodedJWT;
    }
    public static String generateToken(String subject, String role, String issuer, int expireAfter) {
        if(role == null || role.length() == 0) {
            return JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expireAfter))
                    .withIssuer(issuer)
                    .sign(getAlgorithm());
        }
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + time))
                .withIssuer(issuer)
                .withClaim(JwtUtil.ROLE_CLAIM_KEY, role) //get first role in Authorities
                .sign(getAlgorithm());
    }

    public static String generateTokenByAccount(Account account, int expireAfter) {
        return JWT.create()
                .withSubject(String.valueOf(account.getId()))
                .withSubject(String.valueOf(account.getUsername()))
                .withExpiresAt(new Date(System.currentTimeMillis() + time))
                .withIssuer(DEFAULT_ISSUER)
                .withClaim(JwtUtil.ROLE_CLAIM_KEY, account.getRole() == Enums.AccountSRole.ADMIN ? "ADMIN" : "USER" )
                .withClaim("username", account.getUsername())
                .sign(getAlgorithm());
    }

    public static <T extends Enum<?>> T searchEnum(Class<T> enumeration, String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

}
