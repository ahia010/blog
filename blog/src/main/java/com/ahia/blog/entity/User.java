package com.ahia.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;

/**
 *  实体类。
 *
 * @author ahia
 * @since 2024-05-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Column(ignore = true)
    private String newPassword;


    private String avatar;

    private String email;

    private String phone;

    @Column(ignore = true)
    private MultipartFile file;

    /**
     * 权限组
     */
    private Integer role;

    private Integer status;

    @Column(ignore = true)
    private String token;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String note;

}
