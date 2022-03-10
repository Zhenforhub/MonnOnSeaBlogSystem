package com.forq.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
/**
 * 新增这个类是为了在前端展示 标签的信息
 */
public class TagVo {
    private String name;
    private Long count;
}
