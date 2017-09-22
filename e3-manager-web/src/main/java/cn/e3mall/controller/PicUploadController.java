package cn.e3mall.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.utils.FastDFSClient;
import cn.e3mall.utils.JsonUtils;

@Controller
public class PicUploadController {
	
	/**
	 * 图片服务器ip
	 */
	@Value("${IMG_SERVER_URL}")
	private String IMG_SERVER_URL;
	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")//指定返回的类型为utf-8格式。
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		try {
			FastDFSClient fs = new FastDFSClient("classpath:conf/fastdfs.conf");
			//获得文件上传的字节对象
			byte[] bytes = uploadFile.getBytes();
			//获得文件的扩展名
			String extName = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
			String url = fs.uploadFile(bytes, extName);
			url = IMG_SERVER_URL + url;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", 1);
			map.put("url", "图片上传失败");
			return JsonUtils.objectToJson(map);
		}
	}
}
