package com.forq.demo.vo;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 我想知道 Menu类究竟有什么用？
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Menu {

    private String name;//菜单名
    private String url;//菜单url

}
