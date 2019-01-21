/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  com4t
 * Created: Feb 12, 2018
 */

DROP TABLE IF EXISTS `report_config`;
CREATE TABLE IF NOT EXISTS `report_config` (
  `id` bigint(20) NOT NULL,
  `active_status` bit(1) NOT NULL,
  `created_by_user` varchar(255) DEFAULT NULL,
  `creation_time` varchar(255) DEFAULT NULL,
  `modification_time` varchar(255) DEFAULT NULL,
  `modified_by_user` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `report_columns` longtext,
  `hql_query` longtext,
  `name` varchar(50) DEFAULT NULL,
  `native_query` longtext,
  `unit_query` bit(1) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `report_config`
--

INSERT INTO `report_config` (`id`, `active_status`, `created_by_user`, `creation_time`, `modification_time`, `modified_by_user`, `version`, `report_columns`, `hql_query`, `name`, `native_query`, `unit_query`) VALUES
(1, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/10 12:08:29', 'kinah', 1, 'UnitCode, UnitName, LocationName, ExpiryDate, BatchNumber, quantity, StockStatus, LastReceiptCostRate, WAC, StockManufacturer, GenericStockName, UnitOfMesure', NULL, 'CurrentStockReport', 'SELECT u.unit_code UnitCode, u.name UnitName, ln.name LocationName, cs.expiry_date ExpiryDate, cs.batch_number BatchNumber, cs.quantity , cs.stock_status StockStatus, st.last_receipt_cost_rate LastReceiptCostRate, st.waited_avarage_cost WAC, sm.name StockManufacturer, gst.name GenericStockName, uom.name UnitOfMesure  From current_stock AS cs Inner Join stock_type AS st ON cs.stock_type_id = st.id Inner Join unit AS u ON cs.unit_id = u.id Inner Join location AS l ON l.unit_id = u.id AND cs.location_id = l.id Inner Join location_name AS ln ON l.location_name_id = ln.id Inner Join stock_manufacturer AS sm ON st.stock_manufacturer_id = sm.id Inner Join generic_stock_type AS gst ON st.generic_stock_type_id = gst.id Inner Join unit_of_measure AS uom ON st.unit_of_measure_id = uom.id Where cs.quantity > 0  And  u.unit_code = :unitCode AND ln.name = :ln.name', b'1'),
(2, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/01 15:04:49', 'kinah', 1, 'UnitCode, UnitName, LocationName, ExpiryDate, BatchNumber, quantity, StockStatus, LastReceiptCostRate, WAC, StockManufacturer, GenericStockName, UnitOfMesure', NULL, 'ExpiredDrugReport', ' SELECT u.unit_code UnitCode, u.name UnitName, ln.name LocationName, cs.expiry_date ExpiryDate, cs.batch_number BatchNumber, s.quantity , cs.stock_status StockStatus, st.last_receipt_cost_rate LastReceiptCostRate, st.waited_avarage_cost WAC, sm.name StockManufacturer, gst.name GenericStockName, uom.name UnitOfMesure  FROM current_stock AS cs Inner Join stock_type AS st ON cs.stock_type_id = st.id Inner Join unit AS u ON cs.unit_id = u.id Inner Join location AS l ON l.unit_id = u.id AND cs.location_id = l.id Inner Join location_name AS ln ON l.location_name_id = ln.id Inner Join stock_manufacturer AS sm ON st.stock_manufacturer_id = sm.id Inner Join generic_stock_type AS gst ON st.generic_stock_type_id = gst.id Inner Join unit_of_measure AS uom ON st.unit_of_measure_id = uom.id Where cs.quantity > 0  And cs.expiry_date <= Now() And (1=1 Or u.unit_code = :UnitCode) ', b'0'),
(3, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/01 15:04:49', 'kinah', 1, 'UnitCode, UnitName, LocationName, ExpiryDate, BatchNumber, quantity, StockStatus, LastReceiptCostRate, WAC, StockManufacturer, GenericStockName, UnitOfMesure', NULL, 'ItemsNearingExpiryReport', ' SELECT u.unit_code UnitCode, u.name UnitName, ln.name LocationName, cs.expiry_date ExpiryDate, cs.batch_number BatchNumber, cs.quantity , cs.stock_status StockStatus, st.last_receipt_cost_rate LastReceiptCostRate, st.waited_avarage_cost WAC, sm.name StockManufacturer, gst.name GenericStockName, uom.name UnitOfMesure  FROM current_stock AS cs  Inner Join stock_type AS st ON cs.stock_type_id = st.id  Inner Join unit AS u ON cs.unit_id = u.id  Inner Join location AS l ON l.unit_id = u.id AND cs.location_id = l.id  Inner Join location_name AS ln ON l.location_name_id = ln.id  Inner Join stock_manufacturer AS sm ON st.stock_manufacturer_id = sm.id  Inner Join generic_stock_type AS gst ON st.generic_stock_type_id = gst.id  Inner Join unit_of_measure AS uom ON st.unit_of_measure_id = uom.id  WHERE cs.quantity > 0  And  cs.expiry_date >= Now()  And  cs.expiry_date <= DATE_ADD(Now(), INTERVAL 30 DAY) And (1=1 Or u.unit_code = :UnitCode) ', b'0'),
(4, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/10 11:30:22', 'kinah', 1, 'OrderId, RequestDate, Supplier, RequestingUnit, IssuingUnit, RequestingLocation, IssuingLocation, Remarks, WorkFlow, CloseDate, ActiveStatus, GenericStockName, RequestQuantity', NULL, 'PurchaseOrderListReport', ' SELECT sr.id AS OrderId, sr.request_date AS RequestDate, ss.name AS Supplier, ReqU.name AS RequestingUnit, IssU.name AS IssuingUnit, ReqLN.name AS RequestingLocation, IssLN.name AS IssuingLocation, sr.remarks Remarks, sr.work_flow_type WorkFlow, sr.close_date AS CloseDate, sr.active_status AS ActiveStatus, generic_stock_type.name AS GenericStockName, sri.quantity RequestQuantity   From stock_request AS sr  Inner Join stock_request_item AS sri ON sri.stock_request_id = sr.id  Inner Join unit AS ReqU ON sr.request_unit_id = ReqU.id  Inner Join unit AS IssU ON sr.issue_unit_id = IssU.id  Inner Join location AS ReqL ON ReqL.unit_id = ReqU.id AND sr.location_id = ReqL.id  Inner Join location AS IssL ON IssL.unit_id = IssU.id AND sr.issue_location_id = IssL.id  Inner Join location_name AS ReqLN ON ReqL.location_name_id = ReqLN.id  Inner Join location_name AS IssLN ON IssL.location_name_id = IssLN.id  Inner Join stock_supplier AS ss ON sr.stock_supplier_id = ss.id  Inner Join generic_stock_type ON sri.generic_stock_type_id = generic_stock_type.id  Where  sr.work_flow_type = ''PURCHASEORDER''  And  ReqU.unit_code  = :UnitCode  ', b'1'),
(5, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/01 15:04:49', 'kinah', 1, 'OrderId, TransactionDate, RequestingUnit, IssuingUnit, RequestingLocation, IssuingLocation, Remarks, StockTransactionFlow, ActiveStatus, RequestQuantity, Manufacturer, UnitOfMesure, GenericStockName', NULL, 'StockMovementReport', ' SELECT st.id AS OrderId, st.transaction_date AS TransactionDate, ReqU.name AS RequestingUnit, IssU.name AS IssuingUnit, ReqLN.name AS RequestingLocation, IssLN.name AS IssuingLocation, st.remarks AS Remarks, st.stock_transaction_type AS StockTransactionFlow, st.active_status AS ActiveStatus, stl.quantity AS RequestQuantity, sman.name Manufacturer, uom.name UnitOfMesure, gst.name GenericStockName   From stock_transaction AS st  Inner Join stock_transaction_line AS stl ON stl.stock_transaction_id = st.id   Inner Join unit AS ReqU ON st.unit_from_id = ReqU.id  Inner Join unit AS IssU ON st.unit_to_id = IssU.id  Inner Join location AS ReqL ON ReqL.unit_id = ReqU.id AND st.location_from_id = ReqL.id  Inner Join location AS IssL ON IssL.unit_id = IssU.id AND st.location_to_id = IssL.id  Inner Join location_name AS ReqLN ON ReqL.location_name_id = ReqLN.id  Inner Join location_name AS IssLN ON IssL.location_name_id = IssLN.id  Inner Join stock_type AS styp ON stl.stock_type_id = styp.id  Inner Join stock_manufacturer AS sman ON styp.stock_manufacturer_id = sman.id  Inner Join unit_of_measure AS uom ON styp.unit_of_measure_id = uom.id  Inner Join generic_stock_type AS gst ON styp.generic_stock_type_id = gst.id  Where (1=1 Or ReqU.unit_code = :UnitCode)  And (1=1 Or  IssU.unit_code = :IssuingUnitCode) ', b'0'),
(6, b'1', 'kinah', '2018/02/01 15:04:49', '2018/02/10 11:57:20', 'kinah', 1, 'Unit Code,Unit Name,Location Name,Expiry Date,Batch Number,Quantity,Stock Status,Last Receipt Cost Rate,WAC,Stock Manufacturer,Generic Stock Name,Unit of Mesure', NULL, 'CurrentStockAsAtReport', ' SELECT u.unit_code UnitCode, u.name UnitName, ln.name LocationName, cs.expiry_date ExpiryDate, cs.batch_number BatchNumber, cs.quantity , cs.stock_status StockStatus, st.last_receipt_cost_rate LastReceiptCostRate, st.waited_avarage_cost WAC, sm.name StockManufacturer, gst.name GenericStockName, uom.name UnitOfMesure   From current_stock AS cs  Inner Join stock_type AS st ON cs.stock_type_id = st.id  Inner Join unit AS u ON cs.unit_id = u.id  Inner Join location AS l ON l.unit_id = u.id AND cs.location_id = l.id  Inner Join location_name AS ln ON l.location_name_id = ln.id  Inner Join stock_manufacturer AS sm ON st.stock_manufacturer_id = sm.id  Inner Join generic_stock_type AS gst ON st.generic_stock_type_id = gst.id  Inner Join unit_of_measure AS uom ON st.unit_of_measure_id = uom.id  Where cs.quantity > 0   And u.unit_code = :UnitCode  And ( 1=1 Or   (cs.creation_time <= :CurrentStockDate And cs.stock_status = ''AVAILABLE'')    Or   (cs.creation_time <= :CurrentStockDate And cs.stock_status = ''SPLIT'' And cs.modification_time >= :CurrentStockDate )  )', b'1'),
(7, b'1', 'kinah', '2018/02/02 09:51:36', '2018/02/02 09:51:36', 'kinah', 1, 'q,w', NULL, 'test1', 'select * from report_config where id=:UnitCode', b'0');


DROP TABLE IF EXISTS `report_parameter`;
CREATE TABLE IF NOT EXISTS `report_parameter` (
  `id` bigint(20) NOT NULL,
  `active_status` bit(1) NOT NULL,
  `created_by_user` varchar(255) DEFAULT NULL,
  `creation_time` varchar(255) DEFAULT NULL,
  `modification_time` varchar(255) DEFAULT NULL,
  `modified_by_user` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `default_value` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `is_optional` bit(1) DEFAULT NULL,
  `parameter_type` int(11) DEFAULT NULL,
  `report_config_id` bigint(20) DEFAULT NULL,
  `column_name` varchar(50) DEFAULT NULL,
  `select_sql` longtext,
  `select_value_field` varchar(50) DEFAULT NULL,
  `parameter` varchar(50) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `report_parameter`
--

INSERT INTO `report_parameter` (`id`, `active_status`, `created_by_user`, `creation_time`, `modification_time`, `modified_by_user`, `version`, `default_value`, `name`, `is_optional`, `parameter_type`, `report_config_id`, `column_name`, `select_sql`, `select_value_field`, `parameter`) VALUES
(10, b'1', 'kinah', '2018/02/09 11:43:42', '2018/02/10 12:08:29', 'kinah', 1, NULL, 'LocationName', b'1', 3, 1, 'ln.name', 'SELECT  DISTINCT ln.name  From current_stock AS cs Inner Join stock_type AS st ON cs.stock_type_id = st.id Inner Join unit AS u ON cs.unit_id = u.id Inner Join location AS l ON l.unit_id = u.id AND cs.location_id = l.id Inner Join location_name AS ln ON l.location_name_id = ln.id Inner Join stock_manufacturer AS sm ON st.stock_manufacturer_id = sm.id Inner Join generic_stock_type AS gst ON st.generic_stock_type_id = gst.id Inner Join unit_of_measure AS uom ON st.unit_of_measure_id = uom.id Where cs.quantity > 0  And  u.unit_code = :unitCode', 'name', 'parameter1'),
(11, b'1', 'kinah', '2018/02/10 11:23:48', '2018/02/10 11:30:22', 'kinah', 1, NULL, 'RequestDate', b'1', 1, 4, 'sr.request_date', '', '', 'parameter1'),
(13, b'1', 'kinah', '2018/02/10 11:57:20', '2018/02/10 11:57:20', 'kinah', 1, NULL, 'StockDate', b'1', 2, 6, 'CurrentStockDate', '', '', 'parameter1');
