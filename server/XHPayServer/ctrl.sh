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
         nohup java -server -Xmx1024m -Xms800m -Xmn512m -Xss256k -classpath $CURPATH/bin:$CLASSPATH com.core.PayServer ${CURPATH} > ${CURPATH}/nohup.out&
         echo $! > ${CURPATH}/pidfile   #record process id to file   
    # fi  
    ;;
 'stop')
     # Stopping the Service   CURPATH=`cd $(dirname $BASH_SOURCE) && pwd`
    CURPATH=`cd $(dirname $BASH_SOURCE) && pwd`
        PID=`sed -n 1p $CURPATH/pidfile`
	CLASSPATH=
        for i in "$CURPATH"/lib/*.jar; do
           CLASSPATH="$CLASSPATH":"$i"
        done

    if [ ! -z "$PID" ] ; then
               #echo "Stopping the Service, begin killing ${PID}"
                nohup java -classpath $CURPATH/bin:$CLASSPATH com.core.PayServerClose ${CURPATH} > ${CURPATH}/nohup.out&
    fi    
    ;;   
 *)     
     echo "Unmarkable usage: $0 {start|stop}" 
     ;;      
 esac
