FROM maven:3.5.0-jdk-8

ENV TEST_HOME=/app

RUN mkdir -p $TEST_HOME
RUN mkdir -p ~/.m2/repository
WORKDIR $TEST_HOME
COPY pom.xml $TEST_HOME


#RUN mvn -Dmaven.repo.local=/usr/share/maven/ref dependency:get -Dartifact=ru.yandex.qatools.allure:allure-maven-plugin:2.6
RUN mvn -Dmaven.repo.local=/usr/share/maven/ref dependency:resolve
RUN mvn -Dmaven.repo.local=/usr/share/maven/ref dependency:resolve-plugins
RUN mvn -Dmaven.repo.local=/usr/share/maven/ref dependency:go-offline
RUN mvn -Dmaven.repo.local=/usr/share/maven/ref -DskipTests=true test

RUN rm pom.xml
VOLUME $TEST_HOME

CMD mvn -Dmaven.repo.local=/usr/share/maven/ref -Denvironment=$TEST_ENV -DuseRemoteDriver=true -DhubUrl=http://zalenium:4444/wd/hub -Dbrowse=$BROWSER test
