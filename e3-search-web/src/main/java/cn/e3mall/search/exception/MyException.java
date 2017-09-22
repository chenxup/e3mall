package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class MyException implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(MyException.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		//写入日志
		logger.error("系统发生异常", ex);
		
		//展示错误页面
		ModelAndView mv = new ModelAndView();
		mv.setViewName("error/exception");
		return mv;
	}
	
}
