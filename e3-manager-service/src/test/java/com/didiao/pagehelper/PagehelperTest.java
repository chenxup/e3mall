package com.didiao.pagehelper;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PagehelperTest {
	
	//@Test
	public void testPageHelper() throws Exception {
		ApplicationContext ap = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper mapper = ap.getBean(TbItemMapper.class);
		//设置分页参数
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(1, 10);
		
		TbItemExample tbItemExample = new TbItemExample();
		//执行查询
		List<TbItem> list = mapper.selectByExample(tbItemExample );
		
		//获得结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//获得总记录数
		System.out.println(pageInfo.getTotal());
		//获得当前查询的数量
		System.out.println(pageInfo.getSize());
		//获得当前页
		System.out.println(pageInfo.getPageNum());
		
		
	}
}
