package com.example.mobilestore.security;

import com.example.mobilestore.entity.User;
import com.example.mobilestore.exception.InvalidTokenException;
import com.example.mobilestore.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    // Lấy giá trị khóa bí mật từ file cấu hình (application.properties / application.yml)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Lấy giá trị thời gian hết hạn của token từ file cấu hình (tính bằng giây)
    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    /**
     * Phương thức tạo JWT
     * @param authentication Đối tượng Authentication chứa thông tin người dùng
     * @return Token JWT dưới dạng String
     */
    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>(); // Claims có thể chứa thông tin bổ sung
        var user = (User) authentication.getPrincipal(); // Lấy thông tin người dùng từ Authentication

        var expirationTime = new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000L);

        return Jwts.builder()
                .setClaims(claims) // Gán claims vào token
                .setSubject(user.getUsername()) // Thiết lập chủ thể (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian phát hành token
                .setExpiration(expirationTime) // Thời gian hết hạn
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Ký token với thuật toán HS256
                .compact(); // Tạo chuỗi token hoàn chỉnh
    }

    /**
     * Phương thức lấy khóa bí mật dùng để ký JWT
     * @return Key đối tượng dùng để ký JWT
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret); // Giải mã khóa bí mật từ Base64
        return Keys.hmacShaKeyFor(keyBytes); // Trả về khóa được tạo từ mảng byte
    }

    /**
     * Trích xuất username từ token
     * @param token Token JWT
     * @return Username của người dùng
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Subject trong JWT là username
    }

    /**
     * Trích xuất thời gian hết hạn của token
     * @param token Token JWT
     * @return Thời gian hết hạn dưới dạng Date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất một claim cụ thể từ token
     * @param token Token JWT
     * @param claimsResolver Hàm lấy claim cụ thể
     * @return Giá trị của claim được trích xuất
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Trích xuất tất cả claims từ token
     * @param token Token JWT
     * @return Claims chứa tất cả thông tin trong token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey()) // Thiết lập khóa bí mật để xác thực
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); // Lấy phần body chứa thông tin từ JWT
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("JWT token is expired"); // Xử lý khi token hết hạn
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid JWT token"); // Xử lý khi token không hợp lệ
        }
    }

    /**
     * Kiểm tra xem token có hết hạn hay không
     * @param token Token JWT
     * @return true nếu token đã hết hạn, false nếu vẫn còn hiệu lực
     * Giải mã token để lấy username
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // So sánh với thời gian hiện tại
    }

    /**
     * Xác thực tính hợp lệ của token JWT
     * @param token Token JWT
     * @param userDetails Thông tin người dùng từ hệ thống
     * @return true nếu token hợp lệ, false nếu không hợp lệ
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Lấy username từ token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Kiểm tra username và hạn token
    }
}
