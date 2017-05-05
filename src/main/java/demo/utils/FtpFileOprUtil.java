package demo.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FtpFileOprUtil {

	/**
	 * 下载远程文件到本地
	 * @param address
	 * @param localFileName
	 * @return boolean
	 */
	public static boolean download(String downurl, String filename,
			String outfilepath) {

		boolean retcode = false;
		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(downurl);
			File file = new File(outfilepath);
			/* 保存的目录是否存在,不存在则建立 */
			if (!file.exists()) {
				file.mkdirs();
			}
			String outfilename = outfilepath.endsWith("/") ? outfilepath
					+ filename : outfilepath + "/" + filename;
			out = new BufferedOutputStream(new FileOutputStream(outfilename));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			in.close();
			out.close();
			retcode = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			retcode = false;
		}
		return retcode;
	}
}
