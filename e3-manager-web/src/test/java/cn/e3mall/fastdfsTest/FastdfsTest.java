package cn.e3mall.fastdfsTest;

import cn.e3mall.utils.FastDFSClient;

public class FastdfsTest {
	
	//@Test
	public void testupload() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("E:/e3mall/e3-manager-web/src/main/resources/conf/fastdfs.conf");
		String fileName = fastDFSClient.uploadFile("E:\\编程资料\\bookcover\\103.jpg");
		System.out.println(fileName);
		
	}
}
