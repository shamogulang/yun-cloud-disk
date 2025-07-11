
1、启动minio服务：
docker run -d -p 9000:9000 -p 9001:9001 -e MINIO_ROOT_USER=minioadmin  -e MINIO_ROOT_PASSWORD=minioadmin minio/minio:RELEASE.2024-06-11T03-13-30Z server /data --console-address ":9001"

2、启动rocketmq服务
docker run -d --name rmq-namesrv \
-p 9876:9876 \
apache/rocketmq:5.2.0 \
sh mqnamesrv

docker run -d --name rmq-broker \
-p 10911:10911 -p 10909:10909 \
-e "NAMESRV_ADDR=217.60.249.94:9876" \
-e "MAX_POSSIBLE_HEAP=100000000" \
-e "JAVA_OPT_EXT=-Drocketmq.brokerIP1=217.60.249.94" \
-v $(pwd)/broker.conf:/opt/rocketmq-5.2.0/conf/broker.conf \
apache/rocketmq:5.2.0 \
sh mqbroker  -c /opt/rocketmq-5.2.0/conf/broker.conf


3、搭建mysql:然后执行sql脚本：sql/dump-personal_cloud-202507041734.sql
docker run -d \
--name mysql \
-e MYSQL_ROOT_PASSWORD=12345678 \
-e MYSQL_DATABASE=personal_cloud \
-e MYSQL_USER=personal \
-e MYSQL_PASSWORD=12345678 \
-p 3306:3306 \
-v mysql_data:/var/lib/mysql \
mysql:8.0

后面两个步骤都是一样的
3、在Dockerfile文件： docker build -t yun-cloud-disk:1.0.0 .
4、docker run -d -p 8080:8080 --name yun-cloud-disk yun-cloud-disk:1.0.0