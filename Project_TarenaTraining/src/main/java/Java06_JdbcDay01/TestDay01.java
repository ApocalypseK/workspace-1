package Java06_JdbcDay01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;


/**
 * @author Fly Cai / Jul 22, 2017 9:47:52 PM / GuiYang Tarena
 * junit测试方法：
 * 1.方法是公有的，无返回值的，无参数的
 * 2.方法前必须加@Test
 * 3.由于junit用于测试，而测试代码和包
 * 		在正式项目环境中会被抛弃，所以junit
 * 		的包不必使用maven导入也可
 *
 */
public class TestDay01 {

	@Test
	public void test01() throws SQLException{
		/*
		 * 注册驱动，即告诉DriverManager要用哪一个驱动
		 */
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			Class.forName("com.mysql.jdbc.Driver");
			//创建连接
			Connection conn1=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
			System.out.println("Oracle已连接："+conn1);
			Connection conn2=DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbctest","root","root123");
			System.out.println("Mysql已连接："+conn2);
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
