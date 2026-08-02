# 必须钉版本，不能用 latest：国内镜像加速器上的 latest 可能停留在 2021 年的
# 8.3.3，同一份 Dockerfile 在不同机器上会构建出差异巨大的 Grafana。
# 本看板依赖 $__auto(>=10.1)、TraceQL(>=9.4)、structured metadata 派生字段(>=10.1)，
# 日志面板的级别着色与行内菜单则需要 12.x 起默认启用的新版 Logs 面板。
FROM grafana/grafana:13.0.2
ADD ./provisioning /etc/grafana/provisioning
ADD ./grafana.ini /etc/grafana/grafana.ini
ADD ./dashboards /var/lib/grafana/dashboards
