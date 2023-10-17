#!/bin/bash
DEPLOY_HOME=/home/ubuntu/deploy
PROPERTY_HOME=/home/ubuntu/properties

# move to deploy source directory
cd $DEPLOY_HOME/back-edu-s3/src/main/resources
# copy application-prod, application-aws 
cp $PROPERTY_HOME/* .

#  move to gradle build root directory
cd $DEPLOY_HOME/back-edu-s3

# execute gradle build
./gradlew bootWar

# move to War file
cd $DEPLOY_HOME/back-edu-s3/build/libs

# execute War file : option => -Dspring.profiles.active=prod
java -jar -Dspring.profiles.active=prod back-edu-s3-0.0.1-SNAPSHOT.war > edusite.log &



