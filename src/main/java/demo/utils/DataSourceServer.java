package demo.utils;

/**
 * @autho
 * @version  创建时间：2008-12-15 下午04:48:32
 * 类说明
 */
public class DataSourceServer {
    private static ThreadLocal local = new ThreadLocal();    
    public static void putDataSource(String dataSourceName) {    
        local.set(dataSourceName);    
    }    
    public static String getDataSource() {    
        return (String)local.get();    
    }  
}

