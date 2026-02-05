#!/bin/bash

# SkyWalking Agent 下载脚本
# 运行此脚本下载 SkyWalking Java Agent

SKYWALKING_VERSION="9.1.0"
DOWNLOAD_URL="https://archive.apache.org/dist/skywalking/java-agent/${SKYWALKING_VERSION}/apache-skywalking-java-agent-${SKYWALKING_VERSION}.tgz"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "正在下载 SkyWalking Java Agent ${SKYWALKING_VERSION}..."

# 下载 agent
curl -L -o skywalking-agent.tgz "$DOWNLOAD_URL"

# 解压
tar -xzf skywalking-agent.tgz

# 重命名目录
mv skywalking-agent agent

# 清理下载文件
rm skywalking-agent.tgz

# 复制 Spring Cloud Gateway 和 WebFlux 插件到 plugins 目录（默认在 optional-plugins 中）
echo "正在启用 Spring Cloud Gateway 和 WebFlux 插件..."
cp agent/optional-plugins/apm-spring-cloud-gateway-3.x-plugin-*.jar agent/plugins/
cp agent/optional-plugins/apm-spring-webflux-5.x-plugin-*.jar agent/plugins/

# 复制 Trace Ignore 插件并配置忽略路径
echo "正在启用 Trace Ignore 插件..."
cp agent/optional-plugins/apm-trace-ignore-plugin-*.jar agent/plugins/

# 创建忽略路径配置文件
cat > agent/config/apm-trace-ignore-plugin.config << 'EOF'
# 忽略追踪的路径配置
# 支持 Ant 风格路径匹配：
#   ? 匹配单个字符
#   * 匹配任意字符（不包括路径分隔符）
#   ** 匹配任意层级路径

trace.ignore_path=/actuator/**,/health,/health/**,/prometheus,/metrics
EOF

echo "SkyWalking Agent 下载完成！"
echo "Agent 位置: ${SCRIPT_DIR}/agent"
echo "已启用插件: Spring Cloud Gateway 3.x/4.x, Spring WebFlux 5.x, Trace Ignore"
echo "已忽略追踪路径: /actuator/**, /health/**, /prometheus, /metrics"
