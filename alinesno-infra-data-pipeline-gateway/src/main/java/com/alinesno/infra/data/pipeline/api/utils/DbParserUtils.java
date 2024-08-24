package com.alinesno.infra.data.pipeline.api.utils;

import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数据库连接验证
 * 
 * @author luoxiaodong
 * @since 1.0.0
 */
@Slf4j
public class DbParserUtils {

	/**
	 * 解析mysql jdbc链接
	 * @param jdbcUrl
	 */
	public static void parserJdbcUrl(ReaderSourceEntity e , String jdbcUrl) {
		
		String pattern="jdbc:(?<type>[a-z]+)://(?<host>[a-zA-Z0-9-//.]+):(?<port>[0-9]+)/(?<database>[a-zA-Z0-9_]+)?";
        Pattern namePattern = Pattern.compile(pattern);
        Matcher dateMatcher = namePattern.matcher(jdbcUrl);
        
		String dbType = null ; 
		String host = null ; 
		String port = null ; 
		String database = null ; 
		String query = jdbcUrl.substring(jdbcUrl.indexOf("?") + 1); 
        
        while (dateMatcher.find()) {
        	
            dbType = dateMatcher.group("type");
            host = dateMatcher.group("host");
            port = dateMatcher.group("port");
            database = dateMatcher.group("database");
            
            log.debug("dbType = {}" , dbType);
            log.debug("host = {}" , host);
            log.debug("port = {}" , port);
            log.debug("database = {}" , database);
            log.debug("query = {}" , query);

        }
        
        Assert.hasLength(dbType , "数据库类型为空");
        Assert.hasLength(host, "数据IP类型为空");
        Assert.hasLength(port , "数据端口类型为空");
        Assert.hasLength(database , "数据库名类型为空");
       
        e.setReaderPort(port);
        e.setReaderName(database) ;
        e.setReaderUrl(jdbcUrl) ;

	}

}
