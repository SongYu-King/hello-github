package demo.service;

import demo.jdbc.OvertimeDao;
import demo.model.OvertimeModel;

public class OvertimeService {

	/**
	 * @param overtime
	 * @return
	 */
	public int saveOne(OvertimeModel overtime) {
		OvertimeDao overtimeDao = new OvertimeDao();
		return overtimeDao.insertOne(overtime);
	}
}
