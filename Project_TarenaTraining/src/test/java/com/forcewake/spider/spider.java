package com.forcewake.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class spider {

	//定义地址
	private static final String URL="http://www.tooopen.com/view/1439719.html";
//	private static final String URL="http://tmooc.cn/web/series_course.html";
	//获取img标签正则
	private static final String IMGURL_REG="<img.*src=(.*?)[^>]*?>";
	//获取src路径正则
	private static final String IMGSRC_REG="[a-zA-Z]+://[^\\s]*";
	
	//获取html内容
	private String getHtml(String url) throws Exception{
		URL url1=new URL(url);
		URLConnection connection=url1.openConnection();
		BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String text;
		StringBuffer sb=new StringBuffer();
		while((text=br.readLine())!=null){
			sb.append(text,0,text.length());
			sb.append('\n');
		}
		br.close();
		return sb.toString();
	}
	
	//获得ImageUrl地址
	private List<String> getImageUrl(String html){
		Matcher matcher=Pattern.compile(IMGURL_REG).matcher(html);
		List<String> listimgurl=new ArrayList<String>();
		while(matcher.find()){
			listimgurl.add(matcher.group());
		}
		
		return listimgurl;
	}
	
	
	//获得ImageSrc地址
	private List<String> getImageSrc(List<String> listimageurl){
		List<String> listImageSrc=new ArrayList<String>();
		for(String image:listimageurl){
			Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
			while(matcher.find()){
				listImageSrc.add(matcher.group().substring(0, matcher.group().length()-1));
			}
		}
		
		return listImageSrc;
	}
	
	
	//下载图片
	private void Download(List<String> listImgSrc){
		try {
			//开始时间
			Date begindate=new Date();
			for(String url:listImgSrc){
				//开始时间
				Date begindate2=new Date();
				String imageName=url.substring(url.lastIndexOf("/")+1,url.length());
				URL uri=new URL(url);
				InputStream in=uri.openStream();
				FileOutputStream fo=new FileOutputStream(new File("./src/test/resources/spider/imgs/"+imageName));
				byte[] buf=new byte[1024];
				int length=0;
				System.out.println("开始下载："+url);
				while((length=in.read(buf,0,buf.length))!=-1){
					fo.write(buf, 0, length);
				}
				
				in.close();
				fo.close();
				System.out.println(imageName+"下载完成");
				//结束时间
				Date overdate2=new Date();
				double time=overdate2.getTime()-begindate2.getTime();
				System.out.println("用时："+time/1000+"s");
			}
			Date overdate=new Date();
			double time=overdate.getTime()-begindate.getTime();
			System.out.println("总用时："+time/1000+"s");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载失败！");
		}
	}
	
	
	
	public static void main(String[] args) {
		try {
			spider sp=new spider();
			//获得html文本内容
			String HTML=sp.getHtml(URL);
			//获取图片标签
			List<String> imgUrl=sp.getImageUrl(HTML);
			//获取图片地址
			List<String> imgSrc=sp.getImageSrc(imgUrl);
			//下载图片
			sp.Download(imgSrc);
			
		} catch (Exception e) {
			System.out.println("发生错误！");
		}
	}
}
