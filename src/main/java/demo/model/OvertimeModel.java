package demo.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OvertimeModel implements Serializable {

	private Integer id;
	private String user_name; // 加班人员名
	private String time; // 申请时间

	public OvertimeModel() {
	}

	public OvertimeModel(String userName) {
		this.user_name = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
