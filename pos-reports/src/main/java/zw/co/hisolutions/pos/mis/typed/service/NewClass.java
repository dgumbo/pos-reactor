//Select new zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView (  pc.name AS productCategory, 
// sti.name AS stockItem, 
// CASE 
// WHEN 
// COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 8 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END * 8, 
// 1) - cs.quantity > COALESCE(moq.minOrderQuantity, 1) 
// THEN 
// COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 8 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END * 8, 
// 1) - cs.quantity 
// ELSE COALESCE(moq.minOrderQuantity, 1) 
// ENDAS requiredQuantity, 
// COALESCE(sti.lastReceiptCostRate, 0) AS unitCost, 
// COALESCE(sti.lastReceiptCostRate, 0) * CASE 
// WHEN 
// COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 8 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END * 8, 
// 1) - cs.quantity > COALESCE(moq.minOrderQuantity, 1) 
// THEN 
// COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 8 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END * 8, 
// 1) - cs.quantity 
// ELSE COALESCE(moq.minOrderQuantity, 1) 
// END AS orderCost, 
// cs.quantity AS availableQuantity, 
// COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 1 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END, 
// 0) AS averageConsumption, 
// cs.quantity / CASE 
// WHEN 
// SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 1 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END = 0 
// THEN 
// 1 
// ELSE COALESCE(SUM(si.quantity) / CASE 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) <= 0 THEN 1 
// WHEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) < 60 THEN DATEDIFF(CURRENT_TIMESTAMP, MIN(s.sellDate)) 
// ELSE 60 
// END, 
// 1) 
// END AS currentStockDepletionDays, 
// COALESCE(moq.minOrderQuantity, 1) AS minOrderQuantity  ) 
// From zw.co.hisolutions.pos.stocks.entity.CurrentStock AS cs  Join cs.stockItem As sti  Join sti.productCategory As pc  Left Join zw.co.hisolutions.pos.sell.entity.SellItem si On cs.stockItem.id = si.product.id And si.sell.id In ( Select ss.id From zw.co.hisolutions.pos.sell.entity.Sell ss Where ss.sellDate >= :dateBackAgo )  Left Join zw.co.hisolutions.pos.sell.entity.Sell s On s.id = si.sell.id  Left Join zw.co.hisolutions.pos.stocks.entity.MinimumOrderQuantity moq On moq.stockItem.id = cs.stockItem.id  	Where cs.stockStatus= 'AVAILABLE'  And sti.name like :stockItemName  Group By pc.name, 	sti.name, cs.quantity, moq.minOrderQuantity