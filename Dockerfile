FROM registry.cn-hangzhou.aliyuncs.com/jzpub/jdk8-skywalking:1.0
ENV PORT 8080
ENV CLASSPATH /opt/lib
EXPOSE 8080

# copy pom.xml and wildcards to avoid this command failing if there's no target/lib directory
COPY pom.xml target/lib* /opt/lib/

# NOTE we assume there's only 1 jar in the target dir
# but at least this means we don't have to guess the name
# we could do with a better way to know the name - or to always create an app.jar or something
COPY target/*.jar /opt/app.jar
WORKDIR /opt
CMD ["java","-javaagent:/opt/skywalking/agent/skywalking-agent.jar", "-XX:InitialRAMPercentage=50.0", "-XX:MaxRAMPercentage=80.0", "-jar", "app.jar"]