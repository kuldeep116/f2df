#!/bin/sh
#
APP_NAME=F2dfService

APP_LOCATION=/opt
SERVICE_NAME=F2df$APP_NAME
#PATH_TO_JAR=/opt/$APP_NAME
PATH_TO_JAR=$APP_LOCATION/$APP_NAME
JAR_NAME=$APP_NAME.jar
PID_PATH_NAME=/tmp/F2df$APP_NAME-pid

JAVA_OPTS="-Dapp.home=$PATH_TO_JAR -DTitle=$APP_NAME"

USER=installer
LOG_FILE=logs/nohup.log
PATH=$PATH_TO_JAR/:$PATH:$JAVA_HOME

echo "Starting $APP_NAME "
echo $JAVA_OPTS
#echo "script dir $scriptdir"
#cd $SCRIPTPATH
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java $JAVA_OPTS -jar $PATH_TO_JAR/$JAR_NAME >> $PATH_TO_JAR/$LOG_FILE 2>&1 &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill -9 $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill -9 $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup java $JAVA_OPTS -jar $PATH_TO_JAR/$JAR_NAME >> $PATH_TO_JAR/$LOG_FILE 2>&1 &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac