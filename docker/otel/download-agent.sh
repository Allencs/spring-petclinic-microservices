#!/usr/bin/env bash
# 下载 OpenTelemetry Java Agent（零代码自动埋点），由各服务通过 -javaagent 挂载。
set -euo pipefail

VERSION="${OTEL_AGENT_VERSION:-2.12.0}"
DIR="$(cd "$(dirname "$0")" && pwd)"
JAR="$DIR/opentelemetry-javaagent.jar"

if [[ -f "$JAR" ]]; then
  echo "agent already exists: $JAR (set OTEL_AGENT_VERSION + delete to refresh)"
  exit 0
fi

URL="https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${VERSION}/opentelemetry-javaagent.jar"
echo "downloading OpenTelemetry Java Agent v${VERSION} ..."
curl -fL -o "$JAR" "$URL"
echo "downloaded to $JAR"
