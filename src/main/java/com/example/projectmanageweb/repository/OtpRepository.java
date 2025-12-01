package com.example.projectmanageweb.repository;

import java.sql.Timestamp;
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

    // LƯU OTP: map với cột `code`, KHÔNG có is_used
    public int save(Otp otp) {
        String sql = """
            INSERT INTO otps (email, code, created_at, expires_at)
            VALUES (?, ?, ?, ?)
        """;
        return jdbc.update(sql,
                otp.getEmail(),
                otp.getOtpCode(),                           // map vào cột `code`
                Timestamp.from(otp.getCreatedAt()),
                Timestamp.from(otp.getExpiresAt())
        );
    }

    // Lấy OTP hợp lệ theo email + code (chưa hết hạn)
    public Optional<Otp> findByEmailAndOtpCode(String email, String otpCode) {
        String sql = """
            SELECT id, email, code, created_at, expires_at
            FROM otps
            WHERE email = ? 
              AND code = ? 
              AND expires_at >= NOW()
            ORDER BY created_at DESC
            LIMIT 1
        """;

        try {
            Otp otp = jdbc.queryForObject(sql, (rs, rowNum) -> {
                Otp o = new Otp();
                o.setOtpId(rs.getInt("id"));                    // map id
                o.setEmail(rs.getString("email"));
                o.setOtpCode(rs.getString("code"));             // map code
                o.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                o.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
                // bảng không có is_used → cứ để false
                o.setUsed(false);
                return o;
            }, email, otpCode);

            return Optional.ofNullable(otp);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Sau khi dùng OTP thì xóa luôn (thay vì set is_used = TRUE)
    public void markAsUsed(String email, String otpCode) {
        String sql = """
            DELETE FROM otps
            WHERE email = ? AND code = ?
        """;
        jdbc.update(sql, email, otpCode);
    }

    public void deleteExpiredOtps() {
        String sql = "DELETE FROM otps WHERE expires_at < ?";
        jdbc.update(sql, Timestamp.from(Instant.now()));
    }

    public void deleteByEmail(String email) {
        String sql = "DELETE FROM otps WHERE email = ?";
        jdbc.update(sql, email);
    }
}
