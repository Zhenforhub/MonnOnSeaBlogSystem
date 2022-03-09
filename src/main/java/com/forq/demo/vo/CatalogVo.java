package com.forq.demo.vo;

import com.forq.demo.pojo.Catalog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CatalogVo {
    private String username;
    private Catalog catalog;


}
