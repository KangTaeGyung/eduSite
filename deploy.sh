#!/bin/bash
DEPLOY_HOME=/home/ubuntu/deploy
PROPERTY_HOME=/home/ubuntu/properties
TOMCAT_HOME=/opt/apache-tomcat-8.5.78

# move to deploy source directory
cd $DEPLOY_HOME/back-edu-s3/src/main/resources
# copy application-prod, application-aws 
cp $PROPERTY_HOME/* .

#  move to gradle build root directory
cd $DEPLOY_HOME/back-edu-s3

# execute gradle build
#./gradlew war

# rename to War file
cd $DEPLOY_HOME/back-edu-s3/build/libs
mv ./back-edu-s3-0.0.1-SNAPSHOT-plain.war ROOT.war

# shutdown tomcat
cd $TOMCAT_HOME/bin
 ./shutdown.sh

# clear tomcat_home/webapps
cd $TOMCAT_HOME/webapps
rm -r ROOT
rm ROOT.war

# copy tomcat_home/webapps
cp -f $DEPLOY_HOME/back-edu-s3/build/libs/ROOT.war .

# execute War file : option => -Dspring.profiles.active=prod
cd $TOMCAT_HOME/bin
 ./startup.sh


