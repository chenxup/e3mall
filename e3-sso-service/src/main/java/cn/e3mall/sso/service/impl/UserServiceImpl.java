package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.pojo.JsonMap;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.JsonUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSIONID}")
	private String SESSIONID;
	@Value("${SESSIONID_EXPIRE}")
	private int SESSIONID_EXPIRE;

	/**
	 * 验证手机号和用户名或邮箱是否重复
	 */
	public E3Result checkData(String info, int param) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (param == 1) {
			criteria.andUsernameEqualTo(info);
		} else if (param == 2) {
			criteria.andPhoneEqualTo(info);
		} else if (param == 3) {
			criteria.andEmailEqualTo(info);
		} else {
			return E3Result.build(200, "数据参数错误");
		}
		List<TbUser> userList = userMapper.selectByExample(example);
		if (userList != null && userList.size() > 0) {
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	/**
	 * 注册
	 */
	public E3Result register(TbUser user) {
		// 验证表单数据的完整性
		if (StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getUsername())) {
			return E3Result.build(400, "数据不能为空");
		}
		if (!(boolean) checkData(user.getPhone(), 2).getData()) {
			return E3Result.build(400, "手机已存在");
		}
		if (!(boolean) checkData(user.getUsername(), 1).getData()) {
			return E3Result.build(400, "用户名已存在");
		}
		
		//将密码用MD5加密
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		
		//补全信息
		user.setCreated(new Date());
		user.setUpdated(new Date());

		//保存
		userMapper.insert(user);
		return E3Result.build(200, "注册成功");
	}

	/**
	 * 登录
	 */
	public E3Result login(String username, String password) {
		//验证数据不为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return E3Result.build(400, "用户名或密码不能空");
		}
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example );
		//验证用户名
		if (list == null || list.size() == 0) {
			return E3Result.build(400, "用户名不存在");
		}
		TbUser user = list.get(0);
		//验证密码
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!user.getPassword().equals(password)) {
			return E3Result.build(400, "密码不正确");
		}
		
		//仿造session
		String sessionId = UUID.randomUUID().toString().replace("-", "");
		Map<Object, Object> map = new HashMap<>();
		map.put("logUser", user);
		//写入redis
		jedisClient.set(SESSIONID+":" + sessionId, JsonUtils.objectToJson(map));
		//设置过期时间
		jedisClient.expire(SESSIONID+":" + sessionId, SESSIONID_EXPIRE);
		return E3Result.build(200, "登录成功", sessionId);
	}

	/**
	 * 根据token拿到登录用户信息
	 */
	public E3Result getUserByToken(String token) {
		String strlogUser = jedisClient.get(SESSIONID+":" + token);
		if (StringUtils.isBlank(strlogUser)) {
			return E3Result.build(400, "未登录");
		}
		
		Map<String, TbUser> session = JsonUtils.jsonToPojo(strlogUser,Map.class);
		Object logUser = session.get("logUser");
		String str1 = JsonUtils.objectToJson(logUser);
		TbUser jsonToPojo = JsonUtils.jsonToPojo(str1, TbUser.class);
		
		
		
		//重置过期时间
		jedisClient.expire(SESSIONID+":" + token, SESSIONID_EXPIRE);
		return E3Result.ok(jsonToPojo);
	}
	

}
