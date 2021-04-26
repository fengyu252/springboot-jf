package com.zhouwei.springboot.utils;
import java.io.File;  
import java.io.IOException;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.List;


import org.apache.tomcat.util.net.openssl.ciphers.OpenSSLCipherConfigurationParser;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;  
import org.mybatis.generator.config.xml.ConfigurationParser;  
import org.mybatis.generator.exception.InvalidConfigurationException;  
import org.mybatis.generator.exception.XMLParserException;  
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.ClassUtils;

public class MybatisGeneratorUtil {

	
	public static void main(String[] args) {  
       try {  
            System.out.println("start generator ...");  
            List<String> warnings = new ArrayList<String>();  
            boolean overwrite = true;  
            File configFile = new File("E:\\generator.xml");
            		//MybatisGeneratorUtil.class.getResource("/generator.xml").getFile());
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);  
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);  
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);  
            System.out.println("end generator!");  
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
		//System.out.println(MybatisGeneratorUtil.class.getResource("/generator.xml").getFile());
    }  
}
