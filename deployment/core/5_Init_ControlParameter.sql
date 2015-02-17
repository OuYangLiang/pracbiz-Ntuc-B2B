insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1, 'CTRL', 'PWD_EXPIRE_DAYS', NULL, 'Password expire days.', NULL, 90, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(2, 'CTRL', 'EMAIL_EXPIRE_PERIOD', NULL, 'Email expire days.', NULL, 2, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(3, 'CTRL', 'MAX_ATTEMPT_LOGIN', NULL, 'Maximum attempt login times.', NULL, 5, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(4, 'CTRL', 'REPEATED_LOGIN', NULL, 'Allow duplicated login or not.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(5, 'CTRL', 'HELPDESK_NO', NULL, 'Help desk phone.', '18652022500', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(6, 'CTRL', 'HELPDESK_EMAIL', NULL, 'Help desk email.', 'louyang2@pracbiz.com', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(7, 'CTRL', 'MAKER_CHECKER', NULL, 'Maker-Checker enabled.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(8, 'CTRL', 'REQ_INVL', NULL, 'Request interval', NULL, 2, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(9, 'CTRL', 'AUTO_LOGOUT', NULL, 'Auto Logout time.', NULL, 20, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(10, 'CTRL', 'LOG_FILE_NAME', NULL, 'log file name', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(11, 'CTRL', 'LOG_FILE_PATH', NULL, 'log file path', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(12, 'PWD_RULE', 'PWD_MAX_LENGTH', NULL, 'Maximum length of password.', NULL, 12, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(13, 'PWD_RULE', 'PWD_MIN_LENGTH', NULL, 'Minimum length of password.', NULL, 6, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(14, 'PWD_RULE', 'ALPHANUMERIC', NULL, 'Password must be a alphanumeric.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(15, 'PWD_RULE', 'NO_REPEATED_CHARACTER', NULL, 'Do not allow repeated characters.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(16, 'PWD_RULE', 'MIXTURE_CASE', NULL, 'Mixture of upper and lower case alphabets.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(17, 'PWD_RULE', 'SPECIAL_CHARACTER', NULL, 'At least 1 allowable special character.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(18, 'PWD_RULE', 'NOT_IN_DICT_WORD', NULL, 'Must have not be a dictionary password.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, FALSE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(19, 'PWD_RULE', 'NOT_REPEAT_FOR_PWD_CHANGE', NULL, 'Must not exist in password history.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(20, 'PWD_RULE', 'NOT_EQUAL_LOGIN_ID', NULL, 'Password not equal to user id.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(21, 'HSEKEEP', 'PO', 'OUTBOUND', 'Housekeeping days for PO.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(22, 'HSEKEEP', 'GRN', 'OUTBOUND', 'Housekeeping days for GRN.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(23, 'HSEKEEP', 'RTV', 'OUTBOUND', 'Housekeeping days for Rtv.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(24, 'HSEKEEP', 'DN', 'OUTBOUND', 'Housekeeping days for DN.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(25, 'HSEKEEP', 'PN', 'OUTBOUND', 'Housekeeping days for PN.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(26, 'HSEKEEP', 'INV', 'INBOUND', 'Housekeeping days for INV.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(27, 'HSEKEEP', 'DO', 'INBOUND', 'Housekeeping days for DO.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(28, 'HSEKEEP', 'SM', 'OUTBOUND', 'Housekeeping days for SM.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(29, 'HSEKEEP', 'ST', 'OUTBOUND', 'Housekeeping days for ST.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(30, 'CTRL', 'MAX_USER_AMOUNT_FOR_SUPPLIER', NULL, 'max amounts for a supplier', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(31, 'CTRL', 'DEFAULT_PAGE_SIZE', NULL, 'property for grid defaultPageSize', '5', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(32, 'CTRL', 'PAGE_SIZES', NULL, 'property for grid pageSizes', '5,10,15', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(33, 'HSEKEEP', 'UM', 'OUTBOUND', 'Housekeeping days for UM.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(34, 'HSEKEEP', 'SA', 'OUTBOUND', 'Housekeeping days for SA.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(35, 'HSEKEEP', 'DPR', 'OUTBOUND', 'Housekeeping days for DPR.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(36, 'HSEKEEP', 'ITEMIN', 'INBOUND', 'Housekeeping days for ITEMIN.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(37, 'HSEKEEP', 'POCN', 'OUTBOUND', 'Housekeeping days for POCN.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(38, 'CTRL', 'DOCUMENT_WINDOW_BUYER', NULL, 'property of list transaction docs search days', NULL, 1, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(39, 'CTRL', 'DOCUMENT_WINDOW_SUPPLIER', NULL, 'property of list transaction docs search days', NULL, 1, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(40, 'HSEKEEP', 'IM', 'OUTBOUND', 'Housekeeping days for ITEM.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(41, 'HSEKEEP', 'GI', 'OUTBOUND', 'Housekeeping days for GI.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(42, 'HSEKEEP', 'CC', 'OUTBOUND', 'Housekeeping days for CC.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(43, 'HSEKEEP', 'DSD', 'OUTBOUND', 'Housekeeping days for DSD.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(44, 'HSEKEEP', 'CN', 'INBOUND', 'Housekeeping days for Credit Note.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(45, 'CTRL', 'CURRENT_GST', NULL, 'Current GST percent for supplier.', NULL, 7, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(46, 'CTRL', 'NEW_GST', NULL, 'New GST percent for supplier.', NULL, 7, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(47, 'CTRL', 'NEW_GST_FROM_DATE', NULL, 'From Date.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(48, 'CTRL', 'MAX_PDF_COUNT', NULL, 'property of list transaction docs print pdf', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(49, 'CTRL', 'MAX_EXCEL_COUNT', NULL, 'property of list transaction docs export excel', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(50, 'CTRL', 'MAX_SUMMARY_EXCEL_COUNT', NULL, 'property of list transaction docs export sum excel', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(51, 'HSEKEEP', 'SL', 'OUTBOUND', 'Housekeeping days for SL.', NULL, 100, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(52, 'CTRL', 'MAX_DAY_OF_REPORT', NULL, 'Maximum day allowed to export.', NULL, 1, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(53, 'CTRL', 'ZIP_FILE_SIZE_LIMIT', NULL, 'Zip file when size exceed limit.', NULL, 1, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1000, 'EMAIL_CON', 'SMTP_HOST', NULL, 'Smpt host for email.', 'smtp.gmail.com', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1001, 'EMAIL_CON', 'SMTP_PORT', NULL, 'Smpt port for email.', '465', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1002, 'EMAIL_CON', 'SMTP_USER', NULL, 'Smpt user for email.', 'liyong@pracbiz.com', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1003, 'EMAIL_CON', 'SMTP_PASSWORD', NULL, 'Smpt password for email.', 'mailpracbiz', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1004, 'EMAIL_CON', 'SMTP_PROTOCOL', NULL, 'Smpt protocol for email.', 'smtp', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1005, 'EMAIL_CON', 'NEED_AUTH', NULL, 'Needs authorize for email.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1006, 'EMAIL_CON', 'NEED_EHLO', NULL, 'Whether needs ehlo for email.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1007, 'EMAIL_CON', 'CONNECT_TYPE', NULL, 'Email via SSL, TLS or NONE.', "SSL", NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1008, 'EMAIL_CON', 'SOCKET_FACTORY_CLASS', NULL, 'Socket factory class.', 'javax.net.ssl.SSLSocketFactory', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1009, 'EMAIL_CON', 'SOCKET_FACTORY_FALLBACK', NULL, 'Socket factory fall back.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1010, 'EMAIL_CON', 'SENDER_NAME', NULL, 'Email sender name.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1011, 'EMAIL_CON', 'SENDER_ADDR', NULL, 'Email sender address.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1012, 'EMAIL_CON', 'REPLY_TO', NULL, 'Reply to email address.', NULL, NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);


insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1013, 'EMAIL_CON', 'ADMIN_ADDR', NULL, 'Administrator email address.', 'liyong@pracbiz.com,wwyou@pracbiz.com', NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);



insert into CONTROL_PARAMETER(PARAM_OID, SECT_ID, PARAM_ID, CAT_ID, PARAM_DESC, STRING_VALUE, NUM_VALUE, DATE_VALUE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, VALID)
VALUES(1014, 'EMAIL_CON', 'AUTH_MECHANISMS', NULL, 'Authentication mechanisms for email .', "LOGIN", NULL, NULL, now(), 'SYSTEM', NULL, NULL, TRUE);

