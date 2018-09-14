#!/bin/sh   
 #   
 case "$1" in
 'start')
    CURPATH=`cd $(dirname $BASH_SOURCE) && pwd`
    #echo  $CURPATH
        PID=`sed -n 1p $CURPATH/pidfile`
    # if [ ! -z "$PID" ] ; then
    #        echo " ${PID} is running, can't start the server. " 
               #exit 1
    # fi
	CLASSPATH=
	for i in "$CURPATH"/lib/*.jar; do
	   CLASSPATH="$CLASSPATH":"$i"
	done
    # second Starting the Service  
    # if [ some condition here ]; then   
   #    echo "Starting the Service"
         nohup java -server -Xmx256M -Xms256M -Xmn50M -XX:PermSize=30M -XX:MaxPermSize=30M -Xss256K -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -XX:LargePageSizeInBytes=128M -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 -XX:SoftRefLRUPolicyMSPerMB=0 -classpath $CURPATH/bin:$CLASSPATH com.core.GCCServer ${CURPATH} > ${CURPATH}/nohup.out&        
 echo $! > ${CURPATH}/pidfile   #record process id to file   
    # fi  
    ;;
 'stop')
     # Stopping the Service   CURPATH=`cd $(dirname $BASH_SOURCE) && pwd`
    CURPATH=`cd $(dirname $BASH_SOURCE) && pwd`
        PID=`sed -n 1p $CURPATH/pidfile`

    if [ ! -z "$PID" ] ; then
               #echo "Stopping the Service, begin killing ${PID}"
                kill -9 ${PID}
	        echo "Stop complete,  killed ${PID}"
    fi      
    ;;   
 *)     
     echo "Unmarkable usage: $0 {start|stop}" 
     ;;      
 esac
