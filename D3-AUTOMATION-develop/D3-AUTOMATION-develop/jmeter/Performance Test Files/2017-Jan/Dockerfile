FROM openjdk:alpine
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN tar xvzf apache-jmeter-3.1.tgz
ENV PATH=$PATH:/usr/src/myapp/apache-jmeter-3.1/bin
CMD jmeter -n -t perf_test_login.jmx -p nfsTest.config -l /logs/$HOSTNAME.log
