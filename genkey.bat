rem ****************************************************************************
rem *** sample file that prepares to run the ant build up to the last step 
rem ****************************************************************************
rem set JAVA_HOME=c:/Program files/Java/jdk1.6.0_03
rem set ANT_HOME=C:\Program Files\apache-ant-1.7.0 
rem set PATH=%PATH%;%JAVA_HOME\bin;%ANT_HOME%\bin
rem set KEYSTOREPASS=yourpassword

keytool -genkey -keystore bw -alias signjar -storepass %KEYSTOREPASS% -keypass %KEYSTOREPASS% -dname "OU=Development, O=BobbinWork, C=NL"