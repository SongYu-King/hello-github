package demo.utils;


public class SearchProtocolUtil {

	private String protocol = "<Search>";

	public SearchProtocolUtil(String target) {
		protocol += "<Target>" + target + "</Target>";
	}

	public final String toString() {
		return protocol + "</Search>";
	}

	public final void addCondition(String cid, String cidtype, String cidvalue,
			String cidrestr) {
		protocol += "<Condition>" 
				+ "<Cid>" + cid + "</Cid>" 
				+ "<Cidtype>" + cidtype + "</Cidtype>" 
				+ "<Cidvalue>" + cidvalue + "</Cidvalue>" 
				+ "<Cidrestr>" + cidrestr + "</Cidrestr>"
				+ "</Condition>";
	}
}
