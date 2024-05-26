package com.ahia.blog.entity;

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
    @JsonIgnore
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;


    private String avatar;

    @Column(ignore = true)
    private MultipartFile file;

    /**
     * 权限组
     */
    private Integer role;

    private String status;

    @Column(ignore = true)
    private String token;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String note;

}
