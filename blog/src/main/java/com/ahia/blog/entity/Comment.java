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
import java.io.Serial;

/**
 *  实体类。
 *
 * @author ahia
 * @since 2024-05-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "comment")
public class Comment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;


    @JsonIgnore
    private Integer userId;

    @Column(ignore = true)
    private String username;

    @Column(ignore = true)
    private String avatar;

    private Integer postId;

    private String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private Integer replyId;

}
