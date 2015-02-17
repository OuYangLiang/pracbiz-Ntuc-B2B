#! /bin/bash

DIST_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../" && pwd)"
LIB_DIR=$DIST_DIR/lib
CONF_DIR=$DIST_DIR/conf

CLASSPATH=$CONF_DIR:$DIST_DIR/bin

for jar in `find $LIB_DIR $DIST_DIR/bin -type f -name "*.jar"`
do
	CLASSPATH=$CLASSPATH:$jar
done

MAINCLASS=com.pracbiz.client.startup.Bootstrap

java -cp $CLASSPATH $MAINCLASS $1 &
