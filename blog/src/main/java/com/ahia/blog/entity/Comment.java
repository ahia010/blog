package com.ahia.blog.entity;

import com.mybatisflex.annotation.Id;
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

    @Id
    private Integer id;

    private Integer userId;

    private Integer postId;

    private String content;

    private LocalDateTime createTime;

    private Integer replyId;

}
