#! /bin/bash

SCRIPTS_PATH=`pwd`
USER_NAME="b2bportal"
PASSWORD="password"
SCHEMA_NAME="b2bportal"

echo "Initiating database, please wait..."

mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Database_Core_Creation.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Database_Core_Tmp_Creation.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Database_Core_Quartz.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Database_Core_SP.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 1_Init_Oid_Seed.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 2_Init_Country.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 3_Init_Currency.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 4_Init_Module.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 5_Init_ControlParameter.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 6_Init_Operation.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 7_Init_UserType.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 8_Init_SummaryPageSetting.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 9_Init_BusinessRule.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 10_Init_BuyerStore.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < 11_Init_Index.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Z_Init_Record.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Z_Init_Quartz_Record.sql
mysql -u ${USER_NAME} -p${PASSWORD} ${SCHEMA_NAME} < Z_Init_Job_Record.sql

echo "Database has been initiated successfully."
