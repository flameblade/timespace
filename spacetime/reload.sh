#!/bin/bash

echo "PID Check..."
CURRENT_PID=$(ps -ef | grep java | grep spacetime* | awk '{print $2}')
echo "Running PID: {$CURRENT_PID}"
if $CURRENT_PID [ -z CURRENT_PID ];then
   echo "Project is not #running"
else
   kill -9 $CURRENT_PID;
   sleep10;
fi

# 안녕하세요 반갑습니다.!!!
echo "Deploy Project...." nohup java -jar /home/serve/timespace/spacetime/spacetime-0.0.1-SNAPSHOT.jar >> /home/serve/timespace/spacetime/spacetime.log & echo "Done"