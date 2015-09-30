# 桃李街任务调度中心

ScheduleCenter通过`Quartz`框架实现任务调度，通过`Redis`与任务发布者进行通讯。工作流程为：
- 调度中心订阅`Redis`的`Channel`
- 任务发布者向该`Channel`投递消息
- 调度中心收到消息，开始调度

## 通讯协议
```
{
	"type": 1,          // 消息的类型
	"beanName": "",     // 执行任务的springBean
	"cronExp": "",      // crontab表达式
	"exeAt": "",        // 任务执行的时间点
	"parmList": []      // 参数
}
```
`Redis Channel`名: `post-job`