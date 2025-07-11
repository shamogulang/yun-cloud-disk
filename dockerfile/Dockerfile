# 到jar包生成的目录执行：docker build  --no-cache -t oddworldjeffchan/yun-cloud-disk:1.0.0 .
# docker run -d -p 8080:8080 --name yun-cloud-disk oddworldjeffchan/yun-cloud-disk:1.0.0
# jar包放在同个目录下
# 基础镜像，使用官方 JDK8
FROM adoptopenjdk/openjdk8
# 作者信息（可选）
LABEL maintainer="18814098945@163.com"
#镜像端口
EXPOSE 8080
#环境变量
ENV TZ=Asia/Shanghai
ENV applicationName=yun-cloud-disk
ENV ACTIVE_PROFILE=prod
ENV jvm_heap_size="-Xmx256m -Xms256m -Xss256K"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#将编译后的jar放置到镜像根目录
ADD  yun-cloud-disk*.jar /yun-cloud-disk.jar

CMD timestamp=$(date +%Y%m%d%H%M%S) \
&& mkdir -p logs/${applicationName}/$HOSTNAME/gc \
&& java \
${jvm_heap_size} \
-Djava.security.egd=file:/dev/./urandom \
-XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC \
-Xloggc:logs/${applicationName}/$HOSTNAME/gc/${applicationName}-gc-${timestamp}.log \
-jar /yun-cloud-disk.jar