package hexa.org.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	public static String getPropertyString(String fileName)throws IOException{
		String connStr=null;
		Properties props=new Properties();
		FileInputStream fis=new FileInputStream(fileName);
		props.load(fis);
		String user=props.getProperty("username");
		String password=props.getProperty("password");
		String protocol=props.getProperty("protocol");
		String system=props.getProperty("system");
		String database=props.getProperty("dbname");
		String port=props.getProperty("port");
		connStr=protocol+"//"+system+":"+port+"/"+database+"?user="+user+"&password="+password;
		return  connStr;
	}

}