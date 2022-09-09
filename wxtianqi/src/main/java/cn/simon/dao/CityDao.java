package cn.simon.dao;

import cn.simon.entity.CityCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/29 1:03
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Repository
public class CityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CityCode getCityCode(String city){
        String sql = "select citycode from CityCode where city = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(CityCode.class),city);
    }

}
