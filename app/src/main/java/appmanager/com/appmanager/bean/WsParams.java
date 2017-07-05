package appmanager.com.appmanager.bean;

/**
 * Ws参数
 * 
 * @author HeHongxin
 * @date 2016-4-6
 * 
 */
public class WsParams {

	/** 参数json */
	private StringBuilder ps = new StringBuilder();

	/**
	 * 增加参数
	 * 
	 * @param name
	 *            参数名称
	 * @param value
	 *            参数值(不允许包含特殊字符)
	 * @return WsParams
	 */
	public WsParams add(String name, String value) {
		ps.append(",\"").append(name).append("\"");
		ps.append(":\"").append(trim(value)).append("\"");
		return this;
	}

	/**
	 * 获取json格式参数
	 * 
	 * @param acc
	 *            账号
	 * @param ts
	 *            时间戳
	 * @return json
	 */
	public String json(String acc, String ts) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"acc\"").append(":\"").append(acc).append("\"");
		sb.append(",\"ts\"").append(":\"").append(ts).append("\"");
		sb.append(ps);
		sb.append("}");
		return sb.toString();
	}

	/** 格式化参数 */
	private String trim(String value) {
		return null != value ? value.trim() : "";
	}
}
