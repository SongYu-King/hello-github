package demo.jdbc;

import demo.model.OvertimeModel;

public class OvertimeDao extends BaseDao {

	/**
	 * @param overtime
	 * @return
	 */
	public int insertOne(OvertimeModel overtime) {
		int line = 0;
		this.connect();
		try {
			String sql = "INSERT INTO over_time_log(user_name,time) VALUES(?,NOW())";
			pre = con.prepareStatement(sql);
			pre.setString(1, overtime.getUser_name());
			line = pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return line;
	}
}
