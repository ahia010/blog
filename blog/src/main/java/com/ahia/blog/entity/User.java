package com.ahia.blog.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String password;

    /**
     * 权限组
     */
    private Integer role;

    private String status;

    /**
     * 加密盐值
     */
    private String salt;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String note;

}
