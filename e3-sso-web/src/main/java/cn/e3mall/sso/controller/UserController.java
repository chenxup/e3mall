package cn.e3mall.sso.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.sso.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
/*												//指定返回格式为json，并指定编码
	@RequestMapping(value="user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		E3Result result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			String strResult = callback + "(" + JsonUtils.objectToJson(result) + ");";
			return strResult;
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	
	/**
	 * 第二种支持jsonp方式，spring4.1以上才能支持。
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping(value="user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		E3Result result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}

}
