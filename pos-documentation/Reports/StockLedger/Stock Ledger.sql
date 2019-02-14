Declare @StartDate DateTime = '2019-02-01 00:00:00'
Declare @EndDate DateTime = '2019-02-12 23:59:59' 
Declare @StockItem Varchar(255) = 'Pfuko Maheu Traditional 500ml'    ; 

------Declare @StartDate Date = :StartDate 
------Declare @EndDate Date = :EndDate 
------Declare @UnitCode Varchar(255) = :UnitCode 
------Declare @Location Varchar(255) = :Location
------Declare @StockItem Varchar(255) = :StockType 
------Declare @BatchNumber Varchar(255) = ''  ;  
	 
		/* OPENING_STOCK */
		Select @StartDate transaction_time, 'OPENING_STOCK' [Transaction], stock_item_id, quantity
		, Coalesce(cs.quantity, 0) * styp.last_receipt_cost_rate [LRCR Total] 
		, cs.batch_number, cs.consignment cs_consignment, expiry_date
		, styp.name stock_item 
		, 0 [Transaction Count]
		From current_stock AS cs With(NoLock)   
		Inner Join stock_item As styp With(NoLock) On cs.stock_item_id = styp.id    
 
		Where ( 
		 ( cs.creation_time <= @StartDate And cs.stock_status = 'AVAILABLE' ) 		 
		  Or  
		  ( cs.creation_time <= @StartDate And cs.stock_status Like 'SPLIT%' And cs.modification_time >= @StartDate )  )  
		And styp.name Like  '%' +  @StockItem + '%' 

		Union
	 
		/* CLOSSING_STOCK */
		Select @EndDate transaction_time, 'CLOSSING_STOCK' [Transaction], stock_item_id, quantity
		, Coalesce(cs.quantity, 0) * styp.last_receipt_cost_rate [LRCR Total] 
		, cs.batch_number, cs.consignment cs_consignment, expiry_date
		, styp.name stock_item 
		, 0 [Transaction Count]
		From current_stock AS cs With(NoLock)   
		Inner Join stock_item As styp With(NoLock) On cs.stock_item_id = styp.id    
 
		Where ( 
		 ( cs.creation_time <= @EndDate And cs.stock_status = 'AVAILABLE' ) 		 
		  Or  
		  ( cs.creation_time <= @EndDate And cs.stock_status Like 'SPLIT%' And cs.modification_time >= @EndDate )  )  
		And styp.name Like  '%' +  @StockItem + '%' 

		Union

		/* Stock Transactions */
		SELECT st.transaction_time transaction_time
		 
		, st.stock_transaction_type [Transaction]
		, stl.product_id

		, Case 
			When st.stock_transaction_type  In ('ADJUSTMENT_ISSUE', 'STOCK_ISSUE', 'STOCK_SELL', 'STOCK_SELL_TRAY') Then -stl.quantity 
			Else stl.quantity 
		  End Quantity
		, Case 
			When st.stock_transaction_type  In ('ADJUSTMENT_ISSUE', 'STOCK_ISSUE', 'STOCK_SELL', 'STOCK_SELL_TRAY') Then -stl.quantity 
			Else stl.quantity 
		  End * stl.last_receipt_cost_rate [LRCR Total] 
	
		, cs.batch_number
		, cs.consignment cs_consignment
		, cs.expiry_date 
		, styp.name stock_item  
		, 1 [Transaction Count]
		From stock_transaction As st With(NoLock) 
		Inner Join stock_transaction_line AS stl With(NoLock) On stl.stock_transaction_id = st.id 
		Left Join current_stock cs With(NoLock) On cs.id = Coalesce(stl.current_stock_id_transaction_on , stl.current_stock_id_before, stl.current_stock_id_after)
		Left Join current_stock split With(NoLock) On split.id = stl.current_stock_id_after
		   
		Inner Join stock_item As styp With(NoLock) On stl.product_id = styp.id


		Where st.transaction_time >= @StartDate 
		And st.transaction_time <= @EndDate  
		And st.stock_transaction_type Not In ('BOOK_STOCK')
		And styp.name like '%' + @StockItem + '%' 


		Order By styp.name