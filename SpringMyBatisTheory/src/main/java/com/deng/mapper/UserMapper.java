package com.deng.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -06 21:28
 * @Modified By:
 */
public interface UserMapper {
    @Select("select * ")
    public Object getUser();
}
