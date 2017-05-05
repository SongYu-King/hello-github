package demo.utils;


public class CommConstant {

	/* source */
	public static final short SOUCE_IMPS = (short)0;

	public static final short SOUCE_BOSS = (short)1;

	/* type */
	public static final short REQXML_BASE = (short)0;

	public static final short REQXML_BATCH = (short)1;

	public static final short REQXML_ORDER = (short)2;

	public static final short REQXML_BATCHRES = (short)3;

	public static final short REQXML_ORDERRES = (short)4;
	
	public static final short REQXML_ERROR = (short)5;
	

	public static final String REGSOURCE_PC = "1";

	public static final String REGSOURCE_WEB = "2";

	public static final String REGSOURCE_WM = "4";

	public static final String REGSOURCE_SYMBIAN = "6";

	public static final String REGSOURCE_LINUX = "10";

	public static final String REGSOURCE_JAVA = "11";

	public static final String REGSOURCE_WAP = "12";

	public static final String REGSOURCE_SMS = "13";

	public static final String REGSOURCE_STK = "14";

	public static final String REGSOURCE_GW = "19";

	public static final String REGSOURCE_CS = "22";

	public static final String REGSOURCE_GDBATCH = "23";

	public static final String REGSOURCE_BOSS = "24";

	public static final String REGSOURCE_QQ = "25";

	public static final String REGSOURCE_POPO = "26";

	public static final String REGSOURCE_IVR = "27";

	public static final String REGSOURCE_OTHER = "255";

	public static final String REGSOURCE_ALL = "1;2;4;6;10;11;12;13;14;19;22;23;24;25;26;27;255";

	/** 计算手机号码的逻辑poolid的减量 */
	public static final int MPOOL_DECREASE = 134099;

	/* SVCCODE */
	public static final String SVCCODE_FETION_BASE = "1";

	public static final String SVCCODE_FETION_MATCH = "2";

	public static final String SVCCODE_FETION_QQ = "4";

	public static final String SVCCODE_FETION_POPO = "5";

	public static final String SVCCODE_FETION_UID = "27";

	public static final String SVCCODE_FETION_MMAIL = "28";

	public static final String SVCCODE_FETION_VODAFEN = "3";

	/* log level (DEBUG/INFO/WARN/ERROR/FATAL) */
	public static final String LOG_LEVEL_DEBUG = "DEBUG";

	public static final String LOG_LEVEL_INFO = "INFO";

	public static final String LOG_LEVEL_WARN = "WARN";

	public static final String LOG_LEVEL_ERROR = "ERROR";

	public static final String LOG_LEVEL_FATAL = "FATAL";

	/* 运行时异常 */
	public static final String LOG_SORT_REXCEPTION = "-3";

	/* 操作返回异常 */
	public static final String LOG_SORT_EXCEPTION = "-2";

	/* 操作抛出FetionException */
	public static final String LOG_SORT_FE = "-1";

	/* 操作过程中 */
	public static final String LOG_SORT_OPR_PROCESS = "0";

	/* 操作init */
	public static final String LOG_SORT_OPR_INIT = "1";

	/* 操作结果 */
	public static final String LOG_SORT_OPR_RESULT = "2";

	/* 查询的最大数据条数 */
	public static final int CRITERIA_MAXLEN = 100;

	/* http method */
	public static final String HTTP_GET = "GET";

	public static final String HTTP_POST = "POST";

	public static final String SPID_BASE = "901508";

	public static final String SPID_MATCH = "901508";

	public static final String SPID_QQ = "901812";

	public static final String SPID_POPO = "901813";

	public static final String SPID_UID = "123456";

	public static final String SPID_MMAIL = "123456";

	public static final String SPID_VODAFEN = "123456";

	/* 经分数据定购,退订返回编码 */
	public static final String STAT_CODE_OK = "0";

	public static final String STAT_CODE_SYSTEMERROR = "1";

	public static final String STAT_CODE_INVALIDSVCID = "2";

	public static final String STAT_CODE_INVALIDMOBILENO = "3";

	public static final String STAT_CODE_INVALIDREQUESTSOURCE = "4";

	public static final String STAT_CODE_BASESVCNOTSUBSCRIBED = "5";

	public static final String STAT_CODE_SVCALREADYSUBSCRIBED = "6";

	public static final String STAT_CODE_SVCNOTSUBSCRIBED = "7";

	public static final String STAT_CODE_SVCALREADYUNSUBSCRIBED = "8";

	public static final String STAT_CODE_FORBIDDENUSER = "9";

	public static final String STAT_CODE_USERNOTEXISTS = "10";

	public static final String STAT_CODE_USERALREADYDESTROYED = "11";

	public static final String STAT_CODE_USERALREADYSUSPENDED = "12";

	public static final String STAT_CODE_USERALREADYRESUMED = "13";

	public static final String STAT_CODE_BADPASSWORD = "14";

	public static final String BIZCODE_BASE = "IIC";

	public static final String BIZCODE_MATCH = "+MCHAT";

	public static final String BIZCODE_MATCH_URLCODING = "%2bMCHAT";

	public static final String BIZCODE_QQ = "FXQQ";

	public static final String BIZCODE_POPO = "FXPOPO";

	public static final String BIZCODE_MMAIL = "28";

	public static final String BIZCODE_VODAFEN = "29";

	// activitycode.boss.tmonth.bill.req=T0121014
	// activitycode.boss.lmonth.bill.req=T0121015

	// activitycode.check.bill.daily.req=T0120003
	// activitycode.check.bill.monthly.req=T0120012
	// activitycode.check.bill.daily.resp=T0010009
	// activitycode.check.bill.monthly.resp=T0010013

	/* ActivityCode */
	public static final String ACTIVITYCODE_IMPS_REQ = "T2001106";

	public static final String ACTIVITYCODE_BOSS_REQ = "T2001106";

	public static final String ACTIVITYCODE_IMPS_BREQ = "T2101109";

	public static final String ACTIVITYCODE_IMPS_BRES = "T2101110";

	public static final String ACTIVITYCODE_BOSS_BREQ = "T2101109";

	public static final String ACTIVITYCODE_BOSS_BRES = "T2101110";

	public static final String ACTIVITYCODE_BOSS_OREQ = "T2101210";

	public static final String ACTIVITYCODE_BOSS_ORES = "T2101211";

	public static final String ACTIVITYCODE_IMPS_QUERYTRADE_REQ = "T0010010";

	public static final String ACTIVITYCODE_BOSS_NOTICE_REQ = "T0120011";

	// #BIPCode
	// bipcode.boss.tmonth.bill.req=BIP0A008
	// bipcode.boss.lmonth.bill.req=BIP0A009
	// bipcode.boss.notice.req=BIP0C004
	// bipcode.boss.makeup=BIP0A001
	// bipcode.boss.check.bill.daily.req=BIP0A003
	// bipcode.boss.check.bill.daily.res=BIP0A004
	// bipcode.boss.check.bill.monthly.req=BIP0A006
	// bipcode.boss.check.bill.monthly.res=BIP0A007

	/* bipcode */
	public static final String BIPCODE_IMPS_REQ = "BIP2B129";

	public static final String BIPCODE_BOSS_REQ = "BIP2B130";

	public static final String BIPCODE_IMPS_BREQ = "BIP2B133";

	public static final String BIPCODE_IMPS_BRES = "BIP2B133";

	public static final String BIPCODE_BOSS_BREQ = "BIP2B134";

	public static final String BIPCODE_BOSS_BRES = "BIP2B134";

	public static final String BIPCODE_BOSS_OREQ = "BIP2B219";

	public static final String BIPCODE_BOSS_ORES = "BIP2B219";

	public static final String BIPCODE_IMPS_QUERYTRADE_REQ = "BIP0A005";

	public static final String BIPCODE_BOSS_NOTICE_REQ = "BIP0C004";

	public static final String BIPCODE_BOSS_MAKE_UP = "BIP0A001";

	/* imps res */
	public static final String ACTIONCODE_IMPS_RES = "1";

	// #<![CDATA[ XXXXXX ]]>
	// xml.cdata.data=<![CDATA[<?xml version="1.0" encoding="UTF-8"?>{0}]]>
	// xml.cdata.nodata=<![CDATA[]]>
	// xml.cdata.null=<![CDATA[]]>
	// xml.tag=<?xml version="1.0" encoding="UTF-8"?>
	//
	// #send para name
	// xml.data.head=$xmldata=

//	public static final String XML_CDATA_DATA = "<![CDATA[<?xml version=1.0 encoding=UTF-8?>{0}]]>";

	public static final String XML_CDATA_NO_DATA = "<![CDATA[]]>";

	public static final String XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public static final String XML_DATA_HEAD = "$xmldata=";

	// sign.boss.formal=10
	// sign.boss.test=01
	// sign.boss.all=11
	// boss.sign.bipcode=BIP0C001
	// boss.sign.activitycode=T0010006
	// boss.off.bipcode=BIP0C002
	// boss.off.activitycode=T0010007
	// boss.connection.restype=0000
	// boss.connection.rescode=0
	// boss.connection.bipcode=BIP0C003
	// boss.connection.activitycode=T0020008

	public static final String BOSS_CONNECTION_RESTYPE = "0000";

	public static final String BOSS_CONNECTION_RESCODE = "0";

	//	#imps req
	//	routetype.imps.req=01
	//	testflag.imps.req=1
	//	msgsender.imps.req=0001
	//	msgreceiver.imps.req=0001
	//	svccontver.imps.req=0100
	//	idtype.imps.req=01
	//	biztype.imps.req=23
	//	routetype.imps.breq=00
	//	routevalue.imps.breq=00
	//	pkgseq.imps.bres=0022{0}{1}
	//	manageroutevalue.imps.req=990	

	/* imps req */
	public static final String ORIGDOMAIN_IMPS_REQ = "IMPS";

	public static final String HOMEDOMAIN_IMPS_REQ = "BOSS";

	public static final String BIPVER_IMPS_REQ = "0100";

	public static final String ACTIONCODE_IMPS_REQ = "0";

	public static final String ROUTETYPE_IMPS_REQ = "01";

	public static final String TESTFLAG_IMPS_REQ = "1";

	public static final String MSGSENDER_IMPS_REQ = "0001";

	public static final String MSGRECEIVER_IMPS_REQ = "0001";

	public static final String SVCCONTVER_IMPS_REQ = "0100";

	public static final String IDTYPE_IMPS_REQ = "01";

	public static final String BIZTYPE_IMPS_REQ = "23";

	public static final String OPRNUMB_IMPS_REQ = "";

	public static final String ROUTETYPE_IMPS_BREQ = "00";

	public static final String ROUTEVALUE_IMPS_BREQ = "00";

	public static final String MANAGEROUTEVALUE_IMPS_REQ = "";

	public static final String PKGSEQ_IMPS_BRES = "0022{0}{1}";

	/* biztype */
	public static final String BIZTYPE_FETION = "23";

	/* 平台编码 */
	public static final String IMPS_CODE = "0022";

	public static final Integer ID_BOSSLOG_KEY = 1;

	public static final Integer ID_CACHEBOSS_KEY = 2;

	public static final Integer ID_BASEOPRNUMB_KEY = 3;

	public static final Integer ID_BATCHOPRNUMB_KEY = 4;

	public static final Integer ID_ORDEROPRNUMB_KEY = 5;

	public static final Integer ID_TRANSIDH_KEY = 6;

	public static final Integer ID_TRANSIDO_KEY = 7;

	public static final Integer ID_PROCID_KEY = 8;

	public static final Integer ID_PKGSEQ_KEY = 9;

	public static final String GMCFGDB_MID_REGAPI = "23";

	public static final String GMCFGDB_MID_HTPROXY = "33";

	public static final String GMCFGDB_MID_BOSSGW = "06";

	//public static final String URI_REGAPI_USEROPR = "/useropr";
	
	public static final String URI_REGAPI_IMPS = "/imps";
	
	public static final String URI_HTPROXY = "/SubscribeServlet";

	public static final String URI_BOSSGW_OBOSSOPR = "/obossopr";

	public static final String URI_BOSSGW_TRANSFER = "/transfer";

	public static final String URI_BOSSGW_off = "/off";

	public static final String URI_BOSSGW_sign = "/sign";

	public static final String URI_BOSSGW_orgoff = "/orgoff";

	public static final String URI_BOSSGW_orgsign = "/orgsign";

	public static final String URI_BOSSGW_querytrade = "/querytrade";

}
