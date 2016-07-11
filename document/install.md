# Install


## 사전 필요 조건

- JDK 1.7 이상
- Tomcat 7.0.42 버전 이상 (websocket 사용가능한 버전)
- MySQL 5.0.3 이상 (InnoDB engine 이 필요함)

## Download
[https://github.com/OpenSourceConsulting/athena-meerkat/releases](https://github.com/OpenSourceConsulting/athena-meerkat/releases)
에서 아래 파일을 모두 다운로드 한다.

- athena-meerkat-commander-\[최신버전\]-bin.tar.gz
- console.war
- controller.war
- down.zip
- db_init_script.sql

## 초기 database 생성
- database 및 db user 를 아래와 같이 생성한다.

```
shell> mysql -uroot -p<password>
mysql> create database athena_meerkat character set utf8 collate utf8_bin;
mysql> grant all privileges on athena_meerkat.* to meerkat@'%' identified by '<password>';
mysql> quit;
shell> mysql -umeerkat -p<password> athena_meerkat < db_init_script.sql
```

## Installing 
- athena-meerkat-commander-\[최신버전\]-bin.tar.gz 적당한 위치에 압축 해제
- console.war, controller.war, down.zip 파일 모두 설치된 Tomcat의 CATALINA_BASE/webapps 에 압축해제

## Controller 설정변경
- controller/WEB-INF/classes/meerkat-dev.properties 파일의 아래 설정을 환경에 맞게 수정한다.
- mysql db 연결 설정
    - spring.datasource.url : database 연결 url 을 명시한다.
    - spring.datasource.username=db username 을 명시한다.
    - spring.datasource.password=db user password를 명시한다.

 