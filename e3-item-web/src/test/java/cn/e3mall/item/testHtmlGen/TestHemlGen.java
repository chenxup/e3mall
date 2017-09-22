package cn.e3mall.item.testHtmlGen;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestHemlGen {
	@Test
	public void testName() throws Exception {
		//创建Configuration
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件所在的路径
		configuration.setDirectoryForTemplateLoading(new File("E:\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//设置字符集
		configuration.setDefaultEncoding("UTF-8");
		//，加载一个模板。创建一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个模板使用的数据集，可以是pojo也可以是map,一般是map
		Map dataModel = new HashMap<>();
		//加入数据
		dataModel.put("hello", "helloword");
		
		//创建输出的流
		Writer out = new FileWriter("D:\\test\\hello.html");
		//输出文件
		template.process(dataModel, out);
		out.close();
		
		
	}
}
