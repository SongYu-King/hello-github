package demo.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

import java.io.InputStream;
import java.util.Properties;

public class MongoManager {

	private static final MongoManager instance = new MongoManager();

	private boolean inited = false;
	
	private MongoClient client;
	
	private String defaultDb = null;

	private MongoManager() {

	}

	public synchronized void init(String configFile) {
		if(inited) {
			System.out.println("已经初始化");
			return;
		}
		
		InputStream is_ = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configFile);
		Properties pro = new Properties();
		try {
			pro.load(is_);
			@SuppressWarnings("deprecation")
			MongoClientOptions op = MongoClientOptions
					.builder()
					.connectionsPerHost(
							Integer.valueOf(pro.getProperty("conn", "100")))
					.autoConnectRetry(true).build();
			client = new MongoClient(pro.getProperty("host"), op);
			defaultDb = pro.getProperty("db");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		inited = true;
	}

	public static MongoManager getInstance() {
		return instance;
	}
	
	public DB getDB(){
		if(defaultDb != null && !defaultDb.equals("")){
			return client.getDB(defaultDb);
		}
		
		throw new RuntimeException("未指定默认的DB名称，请在配置文件中配置");
	}
	
	public DB getDB(String dbName) {
		if(dbName != null && !dbName.equals("")){
			return client.getDB(dbName);
		}
		throw new RuntimeException("dbName不能为空");
	}
	
	public DBCollection getDBCollection(DB db,String collectionName) {
		if(db == null) {
			throw new RuntimeException("db 不能为空");
		}
		return db.getCollection(collectionName);
	}
}
