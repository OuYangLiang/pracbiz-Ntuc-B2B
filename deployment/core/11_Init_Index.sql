-- PO_INV_GRN_DN_MATCHING
ALTER TABLE PO_INV_GRN_DN_MATCHING ADD INDEX MULTY_INDEX1(MATCHING_STATUS, INV_STATUS, SUPPLIER_STATUS, BUYER_STATUS, PRICE_STATUS, QTY_STATUS, PO_DATE);
ALTER TABLE PO_INV_GRN_DN_MATCHING ADD INDEX MULTY_INDEX2(MATCHING_STATUS, INV_STATUS, SUPPLIER_STATUS, BUYER_STATUS, PRICE_STATUS, QTY_STATUS, PO_NO);