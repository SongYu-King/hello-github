package demo.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;

import java.util.List;

public class MongoUtil {
	
	/**
	 * 多collection进行MapReduce
	 * @param db 执行的库
	 * @param collections collection的列表
	 * @param map map的JS function
	 * @param reduce reduce的JS function
	 * @param out 输出的数据collection
	 * @param initClear 执行mapReduce之前是否进行清除输出collection中数据
	 * @param query 过滤条件
	 * @param sort 排序规则，用于优化
	 */
	public static void mapReduceMutil(DB db, List<String> collections,
			String map, String reduce, String out,boolean initClear,DBObject query,DBObject sort) {
		
		//预先清除out中数据
		if(initClear){
			DBCollection collection = db.getCollection(out);
			collection.drop();
		}
		
		for (String collectionName : collections) {
			DBCollection collection = db.getCollection(collectionName);
			MapReduceCommand command = new MapReduceCommand(collection, map,
					reduce, out, MapReduceCommand.OutputType.REDUCE, query);
			command.setSort(sort);
			collection.mapReduce(command);
		}
	}

}
