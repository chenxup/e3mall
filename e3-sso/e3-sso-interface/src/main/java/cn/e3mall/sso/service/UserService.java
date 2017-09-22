package cn.e3mall.sso.service;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

public interface UserService {
	E3Result checkData(String info, int param);
	E3Result register(TbUser user);
	E3Result login(String username, String password);
	E3Result getUserByToken(String token);
}
