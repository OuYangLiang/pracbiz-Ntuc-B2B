INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(1, 'Outbound_File_Detect_Job', 'B2B-GROUP', 'outboundFileDetectJobTrigger', 'B2B-GROUP', 'The job to detect outbound batch files, such as PO, GRN and so on.', '0 0/10 * * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(2, 'Inbound_File_Detect_Job', 'B2B-GROUP', 'inboundFileDetectJobTrigger', 'B2B-GROUP', 'The job to detect inbound doc files, such as Invoice.', '0 5/10 * * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(3, 'Inbound_Dispatcher_Job', 'B2B-GROUP', 'inboundDispatcherJobTrigger', 'B2B-GROUP', 'The job to move inbound files from buyer on-hold to in folder.', '0 0 0/1 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(4, 'DN_Generator_Job', 'B2B-GROUP', 'dnGeneratorJobTrigger', 'B2B-GROUP', 'The job to generate stock and cost DN by comparing PO, INV and GRN.', '0 0 20 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(5, 'DN_Send_Job', 'B2B-GROUP', 'docSendJobTrigger', 'B2B-GROUP', 'The job to send auto generated DN to suppliers.', '0 0 21 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(6, 'PO_INV_GRN_DN_Matching_Job', 'B2B-GROUP', 'poInvGrnDnMatchingJobTrigger', 'B2B-GROUP', 'The job to match PO, INV, GRN and DN.', '0 0 22 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(7, 'Daily_PO_INV_GRN_DN_Notification_Job', 'B2B-GROUP', 'dailyPoInvGrnDnNotificationJobTrigger', 'B2B-GROUP', 'The job to notify on matching result.', '0 30 23 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(8, 'Supplier_Master_Import_Job', 'B2B-GROUP', 'supplierMasterImportJobTrigger', 'B2B-GROUP', 'The job to import supplier master file.', '30 0 1/2 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(9, 'Store_Import_Job', 'B2B-GROUP', 'storeImportJobTrigger', 'B2B-GROUP', 'The job to import store master file.', '50 0 0/3 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(10, 'User_Import_Job', 'B2B-GROUP', 'userImportJobTrigger', 'B2B-GROUP', 'The job to import user master file.', '0 5 23 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(11, 'Supplier_Admin_Import_Job', 'B2B-GROUP', 'supplierAdminImportJobTrigger', 'B2B-GROUP', 'The job to import supplier admin file.', '0 17 0/1 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(12, 'Daily_PO_Report_Job', 'B2B-GROUP', 'dailyPoReportJobTrigger', 'B2B-GROUP', 'The job to import daily PO report file.', '0 59 23 * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(13, 'Item_Import_Job', 'B2B-GROUP', 'itemImportJobTrigger', 'B2B-GROUP', 'The job to import item file.', '0 0/5 * * * ?', TRUE);


INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(14, 'RTV_DN_Generate_Job', 'B2B-GROUP', 'rtvDnGeneratorJobTrigger', 'B2B-GROUP', 'The job to generate DN from RTV includes export DN.', '0 0/5 * * * ?', TRUE);



INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(15, 'Watsons_Inv_Export_Job', 'B2B-GROUP', 'watsonsInvExportJobTrigger', 'B2B-GROUP', 'The job to export watsons inv file.', '0 59 23 * * ?', TRUE);



INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(16, 'Change_Supplier_Gst_Job', 'B2B-GROUP', 'changeSupplierGstJobTrigger', 'B2B-GROUP', 'The job to change supplier gst percent.', '0 59 23 * * ?', TRUE);



INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(17, 'Database_Upgrade_Job', 'B2B-GROUP', 'databaseUpgradeJobTrigger', 'B2B-GROUP', 'The job to upgrade datebase.', '0 59 23 * * ?', TRUE);



INSERT INTO JOB(JOB_OID, JOB_NAME, JOB_GROUP, TRIGGER_NAME, TRIGGER_GROUP, JOB_DESCRIPTION, CRON_EXPRESSION, ENABLED)
VALUES(18, 'Supplier_Live_Job', 'B2B-GROUP', 'supplierLiveJobTrigger', 'B2B-GROUP', 'The job to make supplier alive.', '0 59 23 * * ?', TRUE);