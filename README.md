

### 环境搭建（Docker快速部署）

#### 1. 安装RabbitMQ

```bash
# 拉取带管理界面的镜像（便于可视化操作）
docker pull rabbitmq:3-management

# 启动容器（默认账号密码 guest/guest，端口 5672 为通信端口，15672 为管理界面）
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

#### 2. 访问管理界面

打开浏览器访问 `http://localhost:15672`，输入账号密码 `guest/guest`，即可看到 RabbitMQ 控制台（可查看队列、交换机、连接等信息）。



### RabbitMQ console配置：

1. **创建交换机（Exchange）**
   - 进入 `Exchanges` 页面，点击 `Add a new exchange`
   - 名称：`m42_test_exchange`，类型：`direct`（直接路由，最简单的类型），其他默认，点击 `Add exchange`
2. **创建队列（Queue）**
   - 进入 `Queues` 页面，点击 `Add a new queue`
   - 名称：`m42_test_queue`，其他默认，点击 `Add queue`
3. **绑定交换机与队列**
   - 在交换机 `m42_test_exchange` 详情页，找到 `Bindings` 区域
   - `To queue` 选择 `m42_test_queue`，`Routing key` 填写`m42_test_key`（路由键，用于匹配消息），点击 `Bind`



### 运行测试

1. 先启动 **消费者** 脚本（保持运行，等待消息）
2. 运行 **生产者** 脚本，发送消息
3. 消费者控制台会输出 `收到消息: Hello, RabbitMQ!`，同时可在 RabbitMQ 管理界面的 `Queues` → `m42_test_queue` 中看到消息已被消费（`Ready` 数量为 0）。