#!/bin/bash

# 1. Tomcat 停止
tcstop

# 2. 旧WARと展開フォルダ削除
rm -rf /opt/tomcat/webapps/sample3.war
rm -rf /opt/tomcat/webapps/sample3

# 3. 新WARコピー
cp ./target/sample3.war /opt/tomcat/webapps/

# 4. Tomcat 起動
/opt/tomcat/bin/startup.sh
