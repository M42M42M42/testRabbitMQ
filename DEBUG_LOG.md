### java.net.ConnectException: Connection refused

说明客户端（你的代码）无法连接到 RabbitMQ 服务器。这通常是**RabbitMQ 服务未启动**或**连接参数错误**导致。

解决步骤：

#### 1. 确认 RabbitMQ 服务是否启动

- **如果用 Docker 部署**：

  执行命令检查容器是否运行：

  ```bash
  docker ps | grep rabbitmq
  ```

  - 若没有输出，说明容器未启动，执行启动命令：

    ```bash
    docker start rabbitmq  # 若容器已创建但停止
    # 若容器未创建，重新执行部署命令：
    docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    ```

#### 2. 验证 RabbitMQ 端口是否可访问

- 访问管理界面确认服务正常：

  打开浏览器访问 `http://localhost:15672`，若能看到登录页面（默认账号 `guest/guest`），说明服务已启动。

  - 若无法访问，说明服务未启动或端口映射错误（Docker 部署时需确保 `-p 15672:15672` 参数正确）。

- 检查 5672 端口是否开放：

  执行命令（Linux/Mac）：

  ```bash
  telnet localhost 5672  # 若显示 "Connected to localhost" 则端口正常
  # 或使用 nc 命令：
  nc -zv localhost 5672
  ```

若提示 “连接失败”，说明端口未开放，需检查 RabbitMQ 配置或防火墙规则。

#### 3. 检查代码中的连接参数

确保代码中的 `host`、`port` 与 RabbitMQ 服务一致：

运行

```java
// 正确的默认参数（本地部署）
factory.setHost("localhost");  // 若 RabbitMQ 在其他机器，需填对应 IP（如 192.168.1.100）
factory.setPort(5672);        // 默认通信端口，若服务修改过端口需同步修改
factory.setUsername("guest"); // 默认用户名
factory.setPassword("guest"); // 默认密码
```

- 注意：`guest` 账号默认只允许本地连接（`localhost` 或 `127.0.0.1`），若 RabbitMQ 部署在远程服务器，需在 RabbitMQ 中配置允许远程访问的账号。

#### 4. 防火墙 / 网络问题

- 若 RabbitMQ 在远程服务器，需确保服务器防火墙开放 5672 端口（通信）和 15672 端口（管理界面）。
- 本地开发时，暂时关闭防火墙测试是否为拦截导致。

(*此处是因为Docker的容器端口和宿主端口设置有问题导致端口对应不上，所以产生连接不上的bug)

### 挂载数据卷

（to be done.....）

#### 代码重构优化

（消费者应该保持连接，同时捕获异常）

（to br done....）