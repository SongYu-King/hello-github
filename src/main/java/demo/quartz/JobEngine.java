package demo.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ClassName：JobEngine
 * 类描述：   集中管理所有定时任务
 * 操作人：wangshuang
 * 操作时间：2015-5-26 下午05:37:56
 *
 * @version 1.0
 */
public class JobEngine {

    private static final Logger logger = LoggerFactory.getLogger(JobEngine.class);
    private static JobEngine job = null;

    private JobEngine() {
    }

    public static JobEngine getInstance() {
        if (job == null)
            return new JobEngine();
        return job;
    }

    public void init() {
        Properties pro = new Properties();
        try {
            pro.load(this.getClass().getResourceAsStream("/quartz.properties"));
            String timer = pro.getProperty("statis_time");
            QuartzManger.addJob(LogStatisJob.class.getName(), LogStatisJob.class, timer);
            //如果有其他定时任务可以继续添加
        } catch (Exception e) {
            logger.error("加载定时任务失败", e);
        }
    }
}
