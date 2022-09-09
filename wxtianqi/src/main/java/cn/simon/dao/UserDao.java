package cn.simon.dao;

import cn.simon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/26 23:59
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteOne(String username){
        String sql = "delete from wxUser where username = ?";
        jdbcTemplate.update(sql,username);
    }

    public void addOne(String username){
        String sql = "insert into wxUser(username) values (?)";
        jdbcTemplate.update(sql,username);
    }

    public void updateOne(String nickname,String birthday,String username){
        String sql = "update wxUser set nickname = ?, birthday = ? where username = ?";
        jdbcTemplate.update(sql,nickname,birthday,username);
    }

    public User getOne(String username){
        String sql = "select * from wxUser where username = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username);
    }

    public void updateOne(String address,String username){
        String sql = "update wxUser set address = ? where username = ?";
        jdbcTemplate.update(sql, address,username);
    }

    public List<User> getAll(){
        String sql = "select * from wxUser where isfrist = '1'";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<User>(User.class));
    }
    public void updateOneStatus(String username){
        String sql = "update wxUser set isfrist = '1' where username = ?";
        jdbcTemplate.update(sql,username);
    }
}
