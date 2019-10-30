package com.yss.schedule.kettle;

import com.yss.schedule.entity.WorkDay;
import com.yss.schedule.mapper.WorkDayDao;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author yss
 */
@Service
public class KettleService {
    @Value("${com.yss.kettle.trans.filepath}")
    private String kettleTransFilePath;

    @Value("${com.yss.kettle.job.dirPath}")
    private String dirPath;
    @Value("${com.yss.kettle.job.jobName}")
    private String jobName;


    @Value("${com.yss.kettle.job.param.FBEGINDATE}")
    private String FBEGINDATE;
    @Value("${com.yss.kettle.job.param.FENDDATE}")
    private String FENDDATE;
    @Value("${com.yss.kettle.job.param.FIN_PF_ID}")
    private String FIN_PF_ID;
    @Value("${com.yss.kettle.job.param.PF_ID}")
    private String PF_ID;
    @Value("${com.yss.kettle.job.param.TA_PF_ID}")
    private String TA_PF_ID;
    @Value("${com.yss.kettle.job.param.FUNDID}")
    private String FUNDID;


    @Value("${com.yss.kettle.host}")
    private String host;
    @Value("${com.yss.kettle.port}")
    private String port;
    @Value("${com.yss.kettle.db}")
    private String db;
    @Value("${com.yss.kettle.userName}")
    private String userName;
    @Value("${com.yss.kettle.password}")
    private String password;

    @Value("${com.yss.kettle.loginName}")
    private String loginName;
    @Value("${com.yss.kettle.loginPassword}")
    private String loginPassword;


    @Autowired
    private WorkDayDao workDayDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(KettleService.class);

    /**
     * 调用kettle的转换
     *
     * @throws KettleException
     */
    public void callTransferTask() throws KettleException {
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(kettleTransFilePath);
        Trans trans = new Trans(transMeta);
        trans.execute(null);
        trans.waitUntilFinished();
    }

    /**
     * 调用kettle的任务
     */
    public void callJobTask() throws KettleException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //创建资源库连接并从资源库中读取kettle任务
        KettleDatabaseRepository repository = RepositoryCon();
        RepositoryDirectoryInterface dir = repository.findDirectory(dirPath);
        ObjectId jobId = repository.getJobId(jobName, dir);
        JobMeta jobMeta = repository.loadJob(jobId, null);
        Job job = new Job(repository, jobMeta);


        if (StringUtils.isEmpty(FENDDATE)) {
            String currentDate = sdf.format(new Date());
            FENDDATE = workDayDao.getWorKDay(currentDate, 1);
        }
        if (StringUtils.isEmpty(workDayDao.isWorkDay(FENDDATE))){
            LOGGER.info("非工作日不执行：FENDDATE:{}",FENDDATE);
            return;
        }
        job.setVariable("FENDDATE", FENDDATE);

        //设置变量
        if (StringUtils.isEmpty(FBEGINDATE)) {
            //如果日期为空，那么默认今天的日期

            FBEGINDATE = workDayDao.getWorKDay(FENDDATE, 5);
        }
        job.setVariable("FBEGINDATE", FBEGINDATE);



        job.setVariable("FIN_PF_ID", FIN_PF_ID);
        job.setVariable("PF_ID", PF_ID);
        job.setVariable("TA_PF_ID", TA_PF_ID);
        job.setVariable("FUNDID",FUNDID);

        LOGGER.info("环境变量：FBEGINDATE:{}，FENDDATE:{}",FBEGINDATE,FENDDATE);

        job.start();
        job.waitUntilFinished();


    }


    /**
     * 获取资源库连接
     *
     * @return
     * @throws KettleException
     */
    private KettleDatabaseRepository RepositoryCon() throws KettleException {
        // 初始化
        KettleEnvironment.init();
        LOGGER.info("kettle环境初始化成功");
        // 数据库连接元对象
        // （kettle数据库连接名称(KETTLE工具右上角显示)，资源库类型，连接方式，IP，数据库名，端口，用户名，密码） //cgmRepositoryConn
        DatabaseMeta databaseMeta = new DatabaseMeta("aaaa", "Oracle", "Native(JDBC)", host,
                db, port, userName, password);
        LOGGER.info("kettle环境初始化成功");
        // 数据库形式的资源库元对象
        KettleDatabaseRepositoryMeta kettleDatabaseRepositoryMeta = new KettleDatabaseRepositoryMeta();
        kettleDatabaseRepositoryMeta.setConnection(databaseMeta);
        // 数据库形式的资源库对象
        KettleDatabaseRepository kettleDatabaseRepository = new KettleDatabaseRepository();
        // 用资源库元对象初始化资源库对象
        kettleDatabaseRepository.init(kettleDatabaseRepositoryMeta);
        LOGGER.info("kettle环境初始化成功");
        // 连接到资源库
        // 默认的连接资源库的用户名和密码
        kettleDatabaseRepository.connect(loginName, loginPassword);
        LOGGER.info("kettle环境初始化成功");
        if (kettleDatabaseRepository.isConnected()) {
            LOGGER.info("连接成功");
            return kettleDatabaseRepository;
        } else {
            throw new RuntimeException("连接失败");
        }
    }
}
