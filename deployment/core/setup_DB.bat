@echo off

set MYSQL_BIN_PATH=D:\Program Files\MySQL\MySQL Server 5.1\bin
set PATH=%MYSQL_BIN_PATH%;%PATH%

set USER_NAME="root"
set PASSWORD="password"
set SCHEMA_NAME="b2bportal"


echo "Initiating database, please wait..."

mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Database_Core_Creation.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Database_Core_Tmp_Creation.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Database_Core_Quartz.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Database_Core_SP.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<1_Init_Oid_Seed.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<2_Init_Country.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<3_Init_Currency.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<4_Init_Module.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<5_Init_ControlParameter.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<6_Init_Operation.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<7_Init_UserType.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<8_Init_SummaryPageSetting.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<9_Init_BusinessRule.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<10_Init_BuyerStore.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<11_Init_Index.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Z_Init_Record.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Z_Init_Quartz_Record.sql
mysql -u root -p%PASSWORD% %SCHEMA_NAME%<Z_Init_Job_Record.sql

echo "Database has been initiated successfully."
