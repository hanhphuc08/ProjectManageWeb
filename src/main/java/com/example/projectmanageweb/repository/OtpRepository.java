package com.example.projectmanageweb.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.Otp;

@Repository
public class OtpRepository {
    private final JdbcTemplate jdbc;
    
    public OtpRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public int save(Otp otp) {
        String sql = """
            INSERT INTO otps (email, otp_code, created_at, expires_at, is_used)
            VALUES (?, ?, ?, ?, ?)
        """;
        return jdbc.update(sql, 
            otp.getEmail(), 
            otp.getOtpCode(), 
            otp.getCreatedAt(), 
            otp.getExpiresAt(), 
            otp.isUsed()
        );
    }
    
    public Optional<Otp> findByEmailAndOtpCode(String email, String otpCode) {
        String sql = """
            SELECT otp_id, email, otp_code, created_at, expires_at, is_used
            FROM otps 
            WHERE email = ? AND otp_code = ? AND is_used = FALSE
            ORDER BY created_at DESC
            LIMIT 1
        """;
        
        try {
            Otp otp = jdbc.queryForObject(sql, (rs, rowNum) -> {
                Otp o = new Otp();
                o.setOtpId(rs.getInt("otp_id"));
                o.setEmail(rs.getString("email"));
                o.setOtpCode(rs.getString("otp_code"));
                o.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                o.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
                o.setUsed(rs.getBoolean("is_used"));
                return o;
            }, email, otpCode);
            
            return Optional.ofNullable(otp);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public void markAsUsed(String email, String otpCode) {
        String sql = """
            UPDATE otps 
            SET is_used = TRUE 
            WHERE email = ? AND otp_code = ? AND is_used = FALSE
        """;
        jdbc.update(sql, email, otpCode);
    }
    
    public void deleteExpiredOtps() {
        String sql = "DELETE FROM otps WHERE expires_at < ?";
        jdbc.update(sql, Instant.now());
    }
    
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM otps WHERE email = ?";
        jdbc.update(sql, email);
    }
}

