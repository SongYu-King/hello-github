package demo.utils;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * FTP远程命令列表<br>
 * USER    PORT    RETR    ALLO    DELE    SITE    XMKD    CDUP    FEAT<br>
 * PASS    PASV    STOR    REST    CWD     STAT    RMD     XCUP    OPTS<br>
 * ACCT    TYPE    APPE    RNFR    XCWD    HELP    XRMD    STOU    AUTH<br>
 * REIN    STRU    SMNT    RNTO    LIST    NOOP    PWD     SIZE    PBSZ<br>
 * QUIT    MODE    SYST    ABOR    NLST    MKD     XPWD    MDTM    PROT<br>
 * 在服务器上执行命令,如果用sendServer来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上\r\n<br>
 * ftpclient.sendServer("XMKD /test/bb\r\n"); //执行服务器上的FTP命令<br>
 * ftpclient.readServerResponse一定要在sendServer后调用<br>
 * nameList("/test")获取指目录下的文件列表<br>
 * XMKD建立目录，当目录存在的情况下再次创建目录时报错<br>
 * XRMD删除目录<br>
 * DELE删除文件<br>
 * <p>Title: 使用JAVA操作FTP服务器(FTP客户端)</p>
 * <p>Description: 上传文件的类型及文件大小都放到调用此类的方法中去检测，
 * 比如放到前台JAVASCRIPT中去检测等
 * 针对FTP中的所有调用使用到文件名的地方请使用完整的路径名（绝对路径开始）。
 * </p>
 *
 * @version 1.0
 */

public class FtpUtil {

    private FtpClient ftpclient;

    private String ipAddress;

    private int ipPort;

    private String userName;

    private String PassWord;

    /**
     * 构造函数
     *
     * @param ip       String 机器IP
     * @param port     String 机器FTP端口号
     * @param username String FTP用户名
     * @param password String FTP密码
     * @throws Exception
     */
    public FtpUtil(String ip, int port, String username, String password)
            throws Exception {
        ipAddress = new String(ip);
        ipPort = port;
        ftpclient = new FtpClient(ipAddress, ipPort);
        userName = new String(username);
        PassWord = new String(password);
    }

    /**
     * 构造函数
     *
     * @param ip       String 机器IP，默认端口为21
     * @param username String FTP用户名
     * @param password String FTP密码
     * @throws Exception
     */
    public FtpUtil(String ip, String username, String password)
            throws Exception {
        ipAddress = new String(ip);
        ipPort = 21;
        ftpclient = new FtpClient(ipAddress, ipPort);
        userName = new String(username);
        PassWord = new String(password);
    }

    /**
     * 登录FTP服务器
     *
     * @throws Exception
     */
    public void login() throws Exception {
        ftpclient.login(userName, PassWord);
    }

    /**
     * 退出FTP服务器
     *
     * @throws Exception
     */
    public void logout() throws Exception {
        /* 用ftpclient.closeServer()
         * 断开FTP出错时用下更语句退出 */
        ftpclient.sendServer("QUIT\r\n");
        /* 取得服务器的返回信息 */
        int reply = ftpclient.readServerResponse();
//		LogUtil.logWrite(Constant.LOG_LEVEL_DEBUG, "FtpUtil", "logout", "",
//				"return code:" + reply,	"", Constant.LOG_SORT_OPR_RESULT);
    }

    /**
     * 在FTP服务器上建立指定的目录,当目录已经存在的情下不会影响目录下的文件,
     * 这样用以判断FTP上传文件时保证目录的存在目录格式必须以"/"根目录开头
     *
     * @param pathList String
     * @throws Exception
     */
    public void buildList(String pathList) throws Exception {
        ftpclient.ascii();
        StringTokenizer s = new StringTokenizer(pathList, "/");
        int count = s.countTokens();
        if (count == 0) {
            return;
        }
        String pathName = "";
        while (s.hasMoreElements()) {
            pathName = pathName + "/" + (String) s.nextElement();
            try {
                ftpclient.sendServer("XMKD " + pathName + "\r\n");
            } catch (Exception e) {
                e = null;
            }
            int reply = ftpclient.readServerResponse();
//    		LogUtil.logWrite(Constant.LOG_LEVEL_DEBUG, "FtpUtil", "buildList", "",
//    				"return code:" + reply,	"", Constant.LOG_SORT_OPR_RESULT);
        }
        ftpclient.binary();
    }

    /**
     * 上传文件到FTP服务器,destination路径以FTP服务器的"/"开始，带文件名、
     * 上传文件只能使用二进制模式，当文件存在时再次上传则会覆盖
     *
     * @param source      String
     * @param destination String
     * @throws Exception
     */
    public void upFile(String source, String destination) throws Exception {
        buildList(destination.substring(0, destination.lastIndexOf("/")));
        ftpclient.binary();
        TelnetOutputStream ftpOut = ftpclient.put(destination);
        TelnetInputStream ftpIn = new TelnetInputStream(new
                FileInputStream(source), true);
        byte[] buf = new byte[204800];
        int bufsize = 0;
        while ((bufsize = ftpIn.read(buf, 0, buf.length)) != -1) {
            ftpOut.write(buf, 0, bufsize);
        }
        ftpIn.close();
        ftpOut.close();
    }

    /**
     * JSP中的流上传到FTP服务器,
     * 上传文件只能使用二进制模式，当文件存在时再次上传则会覆盖
     * 字节数组做为文件的输入流,此方法适用于JSP中通过
     * request输入流来直接上传文件在RequestUpload类中调用了此方法，
     * destination路径以FTP服务器的"/"开始，带文件名
     *
     * @param sourceData  byte[]
     * @param destination String
     * @throws Exception
     */
    public void upFile(byte[] sourceData, String destination) throws Exception {
        buildList(destination.substring(0, destination.lastIndexOf("/")));
        ftpclient.binary();
        TelnetOutputStream ftpOut = ftpclient.put(destination);
        ftpOut.write(sourceData, 0, sourceData.length);
        ftpOut.close();
    }

    /**
     * 从FTP文件服务器上下载文件SourceFileName，到本地destinationFileName
     * 所有的文件名中都要求包括完整的路径名在内
     *
     * @param SourceFileName      String
     * @param destinationFileName String
     * @throws Exception
     */
    public boolean downFile(String SourceFileName, String destinationFileName) {
        boolean retbln = false;
        try {
            ftpclient.binary();
            TelnetInputStream ftpIn = ftpclient.get(SourceFileName);
            byte[] buf = new byte[204800];
            int bufsize = 0;
            FileOutputStream ftpOut = new FileOutputStream(destinationFileName);
            while ((bufsize = ftpIn.read(buf, 0, buf.length)) != -1) {
                ftpOut.write(buf, 0, bufsize);
            }
            ftpOut.close();
            ftpIn.close();
            retbln = true;
        } catch (Exception e) {
//    		LogUtil.logWrite(Constant.LOG_LEVEL_ERROR, "FtpUtil", "downFile", "",
//    				"ex:" + e.getMessage(),	"", Constant.LOG_SORT_OPR_RESULT);
        }
        return retbln;
    }

    /**
     * 从FTP文件服务器上下载文件，输出到字节数组中
     *
     * @param SourceFileName String
     * @return byte[]
     * @throws Exception
     */
    public byte[] downFile(String SourceFileName) throws
            Exception {
        ftpclient.binary();
        TelnetInputStream ftpIn = ftpclient.get(SourceFileName);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byte[] buf = new byte[204800];
        int bufsize = 0;
        while ((bufsize = ftpIn.read(buf, 0, buf.length)) != -1) {
            byteOut.write(buf, 0, bufsize);
        }
        byte[] return_arraybyte = byteOut.toByteArray();
        byteOut.close();
        ftpIn.close();
        return return_arraybyte;
    }

    /**
     * 取得指定目录下的所有文件名，不包括目录名称
     * 分析nameList得到的输入流中的数，得到指定目录下的所有文件名
     *
     * @param fullPath String
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList<String> fileNames(String fullPath) throws Exception {
        /* 字符模式 */
        ftpclient.ascii();
        TelnetInputStream list = ftpclient.nameList(fullPath);
        byte[] names = new byte[2048];
        int bufsize = 0;
        /* 从流中读取 */
        bufsize = list.read(names, 0, names.length);
        list.close();
        ArrayList<String> namesList = new ArrayList<String>();
        int i = 0;
        int j = 0;
        while (i < bufsize) {
        	/* 字符模式为10，二进制模式为13 */
            if (names[i] == 10) { 
            	/* 文件名在数据中开始下标为j,i-j为文件名的长度,文件名在数据中的结束下标为i-1 */
                String tempName = new String(names, j, i - j);
                namesList.add(tempName);
                /* 上一次位置字符模式 */
                j = i + 1;
            }
            i = i + 1;
        }
        return namesList;
    }

    public static void main(String[] args) {
        String url = "192.168.1.203";
        String user = "user";
        String pwd = "my";
        FtpUtil ftpclient = null;
        try {
            ftpclient = new FtpUtil(url, user, pwd);
            ftpclient.login();

            String surl = "/软件下载区/输入法/陈桥智能五笔5.5.rar";
            String durl = "/temp/陈桥智能五笔5.5.rar";
            if (!ftpclient.downFile(surl, durl)) {
                System.out.println("下载失败!");
            } else {
                System.out.println("下载成功!");
            }


            ArrayList<String> al = ftpclient.fileNames("/软件下载区/输入法/");
            if (al.size() > 0) {
                for (int i = 0; i < al.size(); i++) {
                    System.out.println(al.get(i));
                }
            }
            ftpclient.logout();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
