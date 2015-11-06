# 桃李街任务调度中心

## 工作流程
ScheduleCenter通过`Quartz`框架实现任务调度，通过`Redis`与任务发布者进行通讯。工作流程为：
- 调度中心订阅`Redis`的`Channel`: post-job
- 任务发布者向该`Channel`投递消息
- 调度中心收到消息，开始调度
> 任务中心只负责定时触发任务，任务的实际执行者依然是后端业务服务器

## 特性
- 所有任务的执行情况会被记录到数据库中
- 重启程序时自动载入上次没执行的任务
- Web界面管理控制台(developing)

## 通讯协议
```
{
	"type": 1,          // 消息的类型
	"callbackHost",     // 接口主机名
	"callbackPort",     // 主机端口
	"callbackPath",     // 接口URL path
	"callbackMethod",   // 调用方式(GET | POST)
	"cronExp": "",      // crontab表达式
	"exeAt": "",        // 任务执行的时间点
	"parmMap": []       // 自定义参数
}
```

## 可能存在的问题
- `Redis`消息收发可靠性的问题, 考虑使用`Kafka`代替

## 构建执行
```
mvn clean package
./run.sh [web服务监听端口号]
```
