Declare @StartDate DateTime = '2019-02-11 00:00:00'
Declare @EndDate DateTime = '2019-02-12 23:59:59' 
Declare @StockItem Varchar(255) = ''    ; 

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

		Order By styp.name