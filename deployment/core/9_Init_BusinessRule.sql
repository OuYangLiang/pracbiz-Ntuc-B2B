-- Global
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1, 'Global', 'Global', 'AutoLogout', 'Auto logout timeout.', 'BusinessRule.Global.Global.AutoLogout');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2, 'Global', 'Global', 'MatchingJobMinBufferingDays', 'PO-INV-GRN-DN matching job min buffering days.', 'BusinessRule.Global.Global.MatchingJobMinBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3, 'Global', 'Global', 'MatchingJobMaxBufferingDays', 'PO-INV-GRN-DN matching job max buffering days.', 'BusinessRule.Global.Global.MatchingJobMaxBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4, 'Global', 'Global', 'DailyPoReportJobDaysBefore', 'Daily PO report job days before.', 'BusinessRule.Global.Global.DailyPoReportJobDaysBefore');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5, 'Global', 'Global', 'DailyNotificationJobMissingGrnMinBufferingDays', 'Daily notification job min missing GRN buffering days.', 'BusinessRule.Global.Global.DailyNotificationJobMissingGrnMinBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6, 'Global', 'Global', 'DailyNotificationJobMissingGrnMaxBufferingDays', 'Daily notification job max missing GRN buffering days.', 'BusinessRule.Global.Global.DailyNotificationJobMissingGrnMaxBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(8, 'Global', 'Global', 'RTVDnGeneratingJobBuffingDays', 'Auto close no dispute RTV-DN buffering days.', 'BusinessRule.Global.Global.RTVDnGeneratingJobBuffingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(9, 'Global', 'Global', 'DnGeneratingJobMatchingMaxBuffingDays', 'DN generate job matching max buffering days.', 'BusinessRule.Global.Global.DnGeneratingJobMatchingMaxBuffingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(10, 'Global', 'Global', 'DnGeneratingJobMatchingMinBuffingDays', 'DN generate job matching min buffering days.', 'BusinessRule.Global.Global.DnGeneratingJobMatchingMinBuffingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(11, 'Global', 'Global', 'ContinueProcessErrorBatch', 'Mule continue to process batch file with errors.', 'BusinessRule.Global.Global.ContinueProcessErrorBatch');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(12, 'Global', 'Global', 'DailyNotificationJobMissingGiMinBufferingDays', 'Daily notification job min missing Gi buffering days.', 'BusinessRule.Global.Global.DailyNotificationJobMissingGiMinBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(13, 'Global', 'Global', 'DailyNotificationJobMissingGiMaxBufferingDays', 'Daily notification job max missing Gi buffering days.', 'BusinessRule.Global.Global.DailyNotificationJobMissingGiMaxBufferingDays');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(14, 'Global', 'Global', 'RTVDnDisputeAlertWindow', 'RTV-DN dispute alert window.', 'BusinessRule.Global.Global.RTVDnDisputeAlertWindow');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(15, 'Global', 'Global', 'DisableInvoicePaymentInstructions', 'Disable supplier select Payment Instructions of invoice.', 'BusinessRule.Global.Global.DisableInvoicePaymentInstructions');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(16, 'Global', 'Global', 'RtvGiDnQtyTolerance', 'RTV-GI-DN qty tolerance value.', 'BusinessRule.Global.Global.RtvGiDnQtyTolerance');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(17, 'Global', 'Global', 'RtvGiDnQtyToleranceType', 'RTV-GI-DN qty tolerance value type.', 'BusinessRule.Global.Global.RtvGiDnQtyToleranceType');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(18, 'Global', 'Global', 'RtvGiDnPriceTolerance', 'RTV-GI-DN price tolerance value.', 'BusinessRule.Global.Global.RtvGiDnPriceTolerance');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(19, 'Global', 'Global', 'RtvGiDnPriceToleranceType', 'RTV-GI-DN price tolerance value type.', 'BusinessRule.Global.Global.RtvGiDnPriceToleranceType');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(20, 'Global', 'Global', 'RtvGiDnReportGeneratingDateRange', 'RTV-GI-DN Generating Report Date Rage.', 'BusinessRule.Global.Global.RtvGiDnReportGeneratingDateRange');

-- For PO Convert to Invoice
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(100, 'PoConvertInv', 'SorPO', 'QtyEditable', 'Qty can be changed.', 'BusinessRule.PoConvertInv.SorPO.QtyEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(200, 'PoConvertInv', 'SorPO', 'QtylessThanPO', 'Qty must be less than PO', 'BusinessRule.PoConvertInv.SorPO.QtylessThanPO');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(300, 'PoConvertInv', 'SorPO', 'FocQtyEditable', 'Foc Qty can be changed.', 'BusinessRule.PoConvertInv.SorPO.FocQtyEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(400, 'PoConvertInv', 'SorPO', 'FocQtylessThanPO', 'Foc Qty mubt be less than PO', 'BusinessRule.PoConvertInv.SorPO.FocQtylessThanPO');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(500, 'PoConvertInv', 'SorPO', 'UnitPriceEditable', 'Unit Price can be changed.', 'BusinessRule.PoConvertInv.SorPO.UnitPriceEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(600, 'PoConvertInv', 'SorPO', 'UnitPriceLessThanPO', 'Unit Price must be less than PO.', 'BusinessRule.PoConvertInv.SorPO.UnitPriceLessThanPO');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1100, 'PoConvertInv', 'SorPO', 'EmailToStore', 'Send PO alert email to store.', 'BusinessRule.PoConvertInv.SorPO.EmailToStore');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1200, 'PoConvertInv', 'SorPO', 'PdfAsAttachment', 'Use invoice pdf as attachment when alert buyer.', 'BusinessRule.PoConvertInv.SorPO.PdfAsAttachment');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1300, 'PoConvertInv', 'SorPO', 'DiscountEditable', 'Summary Trade discount information can be changed.', 'BusinessRule.PoConvertInv.SorPO.DiscountEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1400, 'PoConvertInv', 'SorPO', 'DiscountForDetailEditable', 'Detail Trade discount information can be changed.', 'BusinessRule.PoConvertInv.SorPO.DiscountForDetailEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1500, 'PoConvertInv', 'SorPO', 'CashDiscountEditable', 'Cash discount information can be changed.', 'BusinessRule.PoConvertInv.SorPO.CashDiscountEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(1600, 'PoConvertInv', 'SorPO', 'IgnoreExpiryDate', 'Ignore expiry date.', 'BusinessRule.PoConvertInv.SorPO.IgnoreExpiryDate');


insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2000, 'PoConvertInv', 'ConPO', 'ItemDiscountEditable', 'Item discount can be changed.', 'BusinessRule.PoConvertInv.ConPO.ItemDiscountEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2100, 'PoConvertInv', 'ConPO', 'ItemAmountEditable', 'Item amount can be changed.', 'BusinessRule.PoConvertInv.ConPO.ItemAmountEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2200, 'PoConvertInv', 'ConPO', 'ItemSharedCostEditable', 'Item shared cost can be changed.', 'BusinessRule.PoConvertInv.ConPO.ItemSharedCostEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2300, 'PoConvertInv', 'ConPO', 'TradeDiscountEditable', 'Trade discount can be changed.', 'BusinessRule.PoConvertInv.ConPO.TradeEditable');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(2400, 'PoConvertInv', 'ConPO', 'CashDiscountEditable', 'Cash discount can be changed.', 'BusinessRule.PoConvertInv.ConPO.CashDiscountEditable');




-- For Autogen DN
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3000, 'Dn', 'Backend', 'AutoGenStockDn', 'Auto generate stock Dn.', 'BusinessRule.Dn.Backend.AutoGenStockDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3100, 'Dn', 'Backend', 'AutoGenCostDn', 'Auto generate cost Dn.', 'BusinessRule.Dn.Backend.AutoGenCostDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3200, 'Dn', 'Backend', 'AutoSendStockDn', 'Auto send out stock Dn.', 'BusinessRule.Dn.Backend.AutoSendStockDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3300, 'Dn', 'Backend', 'AutoSendCostDn', 'Auto send out cost Dn.', 'BusinessRule.Dn.Backend.AutoSendCostDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3500, 'Dn', 'Backend', 'DnNoStyle1', 'DN No. Style --- DN/${storeCode}/${year}/${SequenceNo}', 'BusinessRule.Dn.Backend.DnNoStyle1');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3600, 'Dn', 'Backend', 'UnityFileStype', 'DN Target File for Unity', 'BusinessRule.Dn.Backend.UnityFileStype');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3700, 'Dn', 'Backend', 'NeedTranslate', 'Translate standard DN file into buyer format', 'BusinessRule.Dn.Backend.NeedTranslate');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3800, 'Dn', 'Backend', 'AutoGenDnFromGI', 'Auto generate Dn for RTV', 'BusinessRule.Dn.Backend.AutoGenDnFromGI');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(3900, 'Dn', 'Backend', 'ResolutionRecipients', 'Resolution report recipients.', 'BusinessRule.Dn.Backend.ResolutionRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4000, 'Dn', 'Backend', 'OutstandingRecipients', 'Outstanding report recipients.', 'BusinessRule.Dn.Backend.OutstandingRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4100, 'Dn', 'Backend', 'PriceDiscrepancyRecipients', 'Price Discrepancy report recipients.', 'BusinessRule.Dn.Backend.PriceDiscrepancyRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4200, 'Dn', 'Backend', 'QtyDiscrepancyRecipients', 'Qty Discrepancy report recipients.', 'BusinessRule.Dn.Backend.QtyDiscrepancyRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4300, 'Dn', 'Backend', 'AllowSupplierDisputeMatchingDn', 'Allow Supplier to dispute DN for PO-INV-GRN matching.', 'BusinessRule.Dn.Backend.AllowSupplierDisputeMatchingDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4400, 'Dn', 'Backend', 'DiscrepancyReportToUser', 'Send Discrepancy report to store/GCM users.', 'BusinessRule.Dn.Backend.DiscrepancyReportToUser');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4500, 'Dn', 'Backend', 'AutoCloseAcceptedRecord', 'Auto closing of accepted disputed records.', 'BusinessRule.Dn.Backend.AutoCloseAcceptedRecord');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4600, 'Dn', 'Backend', 'DnExportingRecipients', 'DN Exporting report recipients.', 'BusinessRule.Dn.Backend.DnExportingRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(4700, 'Dn', 'Backend', 'SendResolutionAndOutstandingByGroup', 'Send Resolution and outstanding report by group.', 'BusinessRule.Dn.Backend.SendResolutionAndOutstandingByGroup');

-- For Matching
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5100, 'Matching', 'PoInvGrnDn', 'QtyInvLessGrn', 'Treat it as matched if INV qty < GRN qty.', 'BusinessRule.Matching.PoInvGrnDn.QtyInvLessGrn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5200, 'Matching', 'PoInvGrnDn', 'AmountTolerance', 'Amount tolerance value', 'BusinessRule.Matching.PoInvGrnDn.AmountTolerance');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5400, 'Matching', 'PoInvGrnDn', 'AutoApproveMatchedByDn', 'Auto approve records matched by Debit Note.', 'BusinessRule.Matching.PoInvGrnDn.AutoApproveMatchedByDn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5500, 'Matching', 'PoInvGrnDn', 'MatchedRecipients', 'Matched recipients.', 'BusinessRule.Matching.PoInvGrnDn.MatchedReceipts');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5600, 'Matching', 'PoInvGrnDn', 'UnmatchedRecipients', 'Unmatched recipients.', 'BusinessRule.Matching.PoInvGrnDn.UnmatchedReceipts');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5700, 'Matching', 'PoInvGrnDn', 'EnableSupplierToDispute', 'Enable supplier to dispute.', 'BusinessRule.Matching.PoInvGrnDn.EnableSupplierToDispute');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5800, 'Matching', 'PoInvGrnDn', 'AutoApproveClosedAcceptedRecord', 'Auto export invoices of closed unmatched records approved by buyer.', 'BusinessRule.Matching.PoInvGrnDn.AutoApproveClosedAcceptedRecord');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(5900, 'Matching', 'PoInvGrnDn', 'AutoCloseAcceptedRecord', 'Auto closing of approved unmatched records.', 'BusinessRule.Matching.PoInvGrnDn.AutoCloseAcceptedRecord');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6000, 'Matching', 'PoInvGrnDn', 'ChangeInvDateToGrnDate', 'Change invoice date to the first GRN date.', 'BusinessRule.Matching.PoInvGrnDn.ChangeInvDateToGrnDate');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6100, 'Matching', 'PoInvGrnDn', 'DefaultRecipients', 'Default recipients.', 'BusinessRule.Matching.PoInvGrnDn.DefaultRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6200, 'Matching', 'PoInvGrnDn', 'MatchingJobRecipients', 'Matching job report recipients.', 'BusinessRule.Matching.PoInvGrnDn.MatchingJobRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6300, 'Matching', 'PoInvGrnDn', 'ResolutionRecipients', 'Resolution report recipients.', 'BusinessRule.Matching.PoInvGrnDn.ResolutionRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6400, 'Matching', 'PoInvGrnDn', 'OutstandingRecipients', 'Outstanding report recipients.', 'BusinessRule.Matching.PoInvGrnDn.OutstandingRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6500, 'Matching', 'PoInvGrnDn', 'InvoiceExportingRecipients', 'Invoice exporting report recipients.', 'BusinessRule.Matching.PoInvGrnDn.InvoiceExportingRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6600, 'Matching', 'PoInvGrnDn', 'MissingGrnNotificationRecipients', 'Missing GRN notification recipients.', 'BusinessRule.Matching.PoInvGrnDn.MissingGrnNotificationRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6700, 'Matching', 'PoInvGrnDn', 'PriceInvLessPo', 'Treat it as matched if INV price < PO price.', 'BusinessRule.Matching.PoInvGrnDn.PriceInvLessPo');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6800, 'Matching', 'PoInvGrnDn', 'QtyPoLessGrn', 'Treat it as matched if PO qty < GRN qty.', 'BusinessRule.Matching.PoInvGrnDn.QtyPoLessGrn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(6900, 'Matching', 'PoInvGrnDn', 'SkipMatching', 'Skip do 3 ways matching.', 'BusinessRule.Matching.PoInvGrnDn.SkipMatching');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7000, 'Matching', 'PoInvGrnDn', 'AutoAcceptQtyInvLessGrn', 'Auto Accept if Invoice Qty < GRN Qty.', 'BusinessRule.Matching.PoInvGrnDn.AutoAcceptQtyInvLessGrn');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7100, 'Matching', 'PoInvGrnDn', 'AutoAcceptPriceInvLessPo', 'Auto Accept if Invoice price < PO price.', 'BusinessRule.Matching.PoInvGrnDn.AutoAcceptPriceInvLessPo');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7200, 'Matching', 'PoInvGrnDn', 'MissingGiNotificationRecipients', 'Missing GI notification recipients.', 'BusinessRule.Matching.PoInvGrnDn.MissingGiNotificationRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7300, 'Matching', 'PoInvGrnDn', 'PriceDiscrepancyRecipients', 'Price Discrepancy recipients.', 'BusinessRule.Matching.PoInvGrnDn.PriceDiscrepancyRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7400, 'Matching', 'PoInvGrnDn', 'QtyDiscrepancyRecipients', 'Qty Discrepancy recipients.', 'BusinessRule.Matching.PoInvGrnDn.QtyDiscrepancyRecipients');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7500, 'Matching', 'PoInvGrnDn', 'DiscrepancyReportToUser', 'Send Discrepancy report to store/GCM users.', 'BusinessRule.Matching.PoInvGrnDn.DiscrepancyReportToUser');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7600, 'Matching', 'PoInvGrnDn', 'AutoCloseRejectedRecord', 'Auto closing of rejected unmatched records.', 'BusinessRule.Matching.PoInvGrnDn.AutoCloseRejectedRecord');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7700, 'Matching', 'PoInvGrnDn', 'AutoRejectBuyerLossUnmatchedRecord', 'Auto reject unmatched records which contains items that PO price > INV price or GRN qty > INV qty.', 'BusinessRule.Matching.PoInvGrnDn.AutoRejectBuyerLossUnmatchedRecord');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7800, 'Matching', 'PoInvGrnDn', 'SendResolutionAndOutstandingByGroup', 'Send Resolution and outstanding report by group.', 'BusinessRule.Matching.PoInvGrnDn.SendResolutionAndOutstandingByGroup');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(7900, 'Matching', 'PoInvGrnDn', 'RtvGiDnExceptionReportRecipients', 'RTV-GI-DN Exception Report recipients.', 'BusinessRule.Matching.PoInvGrnDn.RtvGiDnExceptionReportRecipients');

-- For supplier master

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(10000, 'SM', 'Importing', 'GenAdminUser', 'Automatically generate supplier admin user.', 'BusinessRule.SM.Importing.GenAdminUser');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(10100, 'SM', 'Importing', 'AdminRole', 'Admin roles assigned to generated admin user.', 'BusinessRule.SM.Importing.AdminRole');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(10200, 'SM', 'Importing', 'GenResultTxt', 'Output a txt file after generate admin users(is send emails).', 'BusinessRule.SM.Importing.GenResultTxt');

-- For item

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(11000, 'ITEM', 'Importing', 'Update', 'Update item', 'BusinessRule.ITEM.Importing.Update');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(11100, 'ITEM', 'Importing', 'DeleteAndInsert', 'Delete and insert item', 'BusinessRule.ITEM.Importing.DeleteAndInsert');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(11200, 'ITEM', 'Importing', 'SelectAllToCompare', 'Select all to compare whether an item exist', 'BusinessRule.ITEM.Importing.SelectAllToCompare');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(11300, 'ITEM', 'Importing', 'SelectOneToCompare', 'Select one to compare whether an item exist', 'BusinessRule.ITEM.Importing.SelectOneToCompare');

-- For PO

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(12000, 'PO', 'Global', 'DeliveryDateRange', 'PO delivery date range', 'BusinessRule.PO.Global.DeliveryDateRange');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(12001, 'PO', 'Global', 'NeedValidateConsignmentPo', 'Need validate consignment PO Logic', 'BusinessRule.PO.Global.NeedValidateConsignmentPo');

-- For GRN

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(13000, 'GRN', 'Global', 'SupplierCanDisputeGRN', 'Allow supplier to dispute', 'BusinessRule.GRN.Global.AllowSupplierToDispute');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(13100, 'GRN', 'Global', 'PreventItemsNotExistInPO', 'Block GRNs which contain items does not exist in corresponding PO', 'BusinessRule.GRN.Global.PreventItemsNotExistInPO');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(13200, 'GRN', 'Global', 'PreventItemsLessThanPO', 'Block GRNs which items less than corresponding PO', 'BusinessRule.GRN.Global.PreventItemsLessThanPO');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(13300, 'GRN', 'Global', 'PreventItemsQtyMoreThanPO', 'Block GRNs which contain items qty more than corresponding PO', 'BusinessRule.GRN.Global.PreventItemsQtyMoreThanPO');


-- For INV

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(14000, 'INV', 'Global', 'PreventItemsNotExistInPO', 'Block INVs which contain items does not exist in corresponding PO', 'BusinessRule.INV.Global.PreventItemsNotExistInPO');

-- For GI

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(15000, 'GI', 'Global', 'PreventItemsNotExistInRtv', 'Block GIs which contain items does not exist in corresponding RTV', 'BusinessRule.GI.Global.PreventItemsNotExistInRtv');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(15100, 'GI', 'Global', 'PreventItemsLessThanRtv', 'Block GIs which items less than corresponding RTV', 'BusinessRule.GI.Global.PreventItemsLessThanRtv');
insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(15200, 'GI', 'Global', 'PreventItemsQtyMoreThanRtv', 'Block GIs which contain items qty more than corresponding RTV', 'BusinessRule.GI.Global.PreventItemsQtyMoreThanRtv');

-- For DSD

insert into BUSINESS_RULE(RULE_OID, FUNC_GROUP, FUNC_ID, RULE_ID, RULE_DESC, RULE_DESC_KEY)
values(16000, 'DSD', 'Global', 'NeedValidate', 'Need validate Daily Sales Data Logic', 'BusinessRule.GI.Global.NeedValidate'); 