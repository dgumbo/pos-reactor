Declare @StartDate DateTime = '2019-02-01 00:00:00'
Declare @EndDate DateTime = '2019-02-12 23:59:59' 
Declare @StockItem Varchar(255) = ''    ; 

--Declare @StartDate DateTime = :StartDate 
--Declare @EndDate DateTime = :EndDate  
--Declare @StockItem Varchar(255) = :StockType   ;  

/* IF OBJECT_ID('tempdb..#Results') IS NOT NULL
	DROP TABLE #Results */

Select 	[Stock Item], [Batch_Number], [Consignment Stock], [Transaction Date], [Transaction], Quantity Quantity,
	 
	SUM (	Case 
				When [Lag Stock Transaction] = 'STOCK_UPDATE' And [Transaction] In ('CLOSSING_STOCK') Then 0
				When [Lag Stock Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) 
				When [Transaction] In ('OPENING_STOCK', 'CLOSSING_STOCK') Then 0 
				When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT') Then 0 
				When [Transaction] In ('GOODS_RECEIVED_VOUCHER') Then 0 /* Needs Further Testing As This Might be neccesary only When First Trans is GRV */
				Else Coalesce(Quantity, 0) 
			End 
			+
			Case 
				When RN = 1 Then Coalesce(Quantity, 0) 
				When [Transaction] In ('CLOSSING_STOCK') Then 0 
				When [Transaction] In ('BOOK_STOCK', 'STOCK_UPDATE') Then -Coalesce(Quantity, 0)
				When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT', 'GOODS_RECEIVED_VOUCHER') Then Coalesce(Quantity, 0) 
				Else 0 
			End
			+
			Case 
				When [Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) - Coalesce(RunningQuantityTotal, 0) + Coalesce(Quantity, 0)
				Else 0 
			End
		) OVER (Partition By  [Stock Item], [Batch_Number], [Consignment Stock] ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, [Transaction Time]) 
	AS [Running Stock] ,	
	
	/* Not Included In Report * / 
	RunningQuantityTotal,
	LagQuantity, 
	Case 
		When [Lag Stock Transaction] = 'STOCK_UPDATE' And [Transaction] In ('CLOSSING_STOCK') Then 0
		When [Lag Stock Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) 
		When [Transaction] In ('OPENING_STOCK', 'CLOSSING_STOCK') Then 0 
		When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT') Then 0 
		When [Transaction] In ('GOODS_RECEIVED_VOUCHER') Then 0 
		Else Coalesce(Quantity, 0) 
	End [First Clause],
	Case 
		When RN = 1 Then Coalesce(Quantity, 0)
		--When [Lag Stock Transaction] = 'STOCK_UPDATE' Then -Coalesce(Quantity, 0) 
		When [Transaction] In ('CLOSSING_STOCK') Then 0 
		When [Transaction] In ('BOOK_STOCK', 'STOCK_UPDATE') Then -Coalesce(Quantity, 0)
		When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT', 'GOODS_RECEIVED_VOUCHER') Then Coalesce(Quantity, 0) 
		Else 0 
	End  [Second Clause],
	Case 
		When [Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) - Coalesce(RunningQuantityTotal, 0) + Coalesce(Quantity, 0)
		Else 0 
	End [Third Clause], 
	Case 
		When RN = 1 Then Coalesce(Quantity, 0)
		When [Transaction] In ('CLOSSING_STOCK') Then 0 
		When [Transaction] In ('BOOK_STOCK', 'STOCK_UPDATE') Then -Coalesce(Quantity, 0)
		When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT') Then Coalesce(Quantity, 0) 
		Else 0 
	End ssss,  
	Case 
		When [Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) - Coalesce(RunningQuantityTotal, 0) + Coalesce(Quantity, 0)
		Else 0 
	End tttttt,
	[Total Transaction Count] ,	
	/ * End Not Included In Report */ 
	
	[LRCR Total],	 
	[Expiry Date],	
	[Transaction Time],
	[Transaction Count]
/* --	, *
Into #Results */
From (
	Select StockLedger.stock_item  [Stock Item]
		, StockLedger.batch_number [Batch_Number] 
		, Case When StockLedger.cs_consignment = 1 Then 'Yes' Else 'No' End [Consignment Stock]
		, StockLedger.cs_consignment
		, Convert(Date, StockLedger.transaction_time) [Transaction Date] 
		, [Transaction] 
		, Quantity 
		, [LRCR Total]
		, expiry_date [Expiry Date] 
		, StockLedger.transaction_time [Transaction Time]  
		, Row_number() OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) 
			AS RN 
		, SUM (	Quantity ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) 
			AS RunningQuantityTotal 	 
		, LAG (	Quantity ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) 
			AS LagQuantity 
		, LAG (	[Transaction]  ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY  Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) 
			AS [Lag Stock Transaction]  
		, Sum( [Transaction Count] ) Over(Partition By  stock_item,   batch_number, cs_consignment Order By [Transaction])  [Total Transaction Count]
		, [Transaction Count] 
	From (
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
			
	) StockLedger    

	Where StockLedger.stock_item like '%' + @StockItem + '%'  
) shshs

Order By shshs.[Stock Item], shshs.[Batch_Number], [Consignment Stock], Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, shshs.[transaction time]
Go

IF OBJECT_ID('tempdb..#WithClossingStock') IS NOT NULL 
	DROP TABLE #WithClossingStock
Select [Stock Item], [Batch_Number], [Consignment Stock]
Into #WithClossingStock
From #Results 
Where [Transaction] = 'CLOSSING_STOCK'
And Quantity != [Running Stock] 
Print ('Finished Populating #WithClossingStock')
Go

--IF OBJECT_ID('tempdb..#WithOpeningStock') IS NOT NULL
--	DROP TABLE #WithOpeningStock
--Select [Stock Item], [Batch_Number],  [Consignment Stock]
--Into #WithOpeningStock
--From #Results 
--Where [Transaction] = 'OPENING_STOCK'
--And Quantity != [Running Stock]
--Go

--IF OBJECT_ID('tempdb..#WithReversals') IS NOT NULL
--	DROP TABLE #WithReversals
--Select [Stock Item], [Batch_Number], [Consignment Stock]
--Into #WithReversals
--From #Results 
--Where [Transaction] = 'REVERSAL'
--And Quantity != [Running Stock]
--Go


--IF OBJECT_ID('tempdb..#Results3') IS NOT NULL
--	DROP TABLE #Results3
Select * 
--Into #Results3
From #Results r1
Inner Join #WithClossingStock ro2 On r1.[Stock Item] = ro2.[Stock Item] And r1.[Batch_Number] = ro2.[Batch_Number] And r1.[Consignment Stock] = ro2.[Consignment Stock]
--Inner Join #WithOpeningStock rc2 On r1.[Stock Item] = rc2.[Stock Item] And r1.[Batch_Number] = rc2.[Batch_Number] And r1.Unit = rc2.Unit And r1.Location = rc2.Location And r1.[Consignment Stock] = ro2.[Consignment Stock]
--Inner Join #WithReversals rr1 On r1.[Stock Item] = rr1.[Stock Item] And r1.[Batch_Number] = rr1.[Batch_Number] And r1.Unit = rr1.Unit And r1.Location = rr1.Location And r1.[Consignment Stock] = rr1.[Consignment Stock]
--Where rr1.[Stock Item] Is Null
--And [Stock Item] = 'CIMETIDINE 200MG TABS ULCERMED 20s' 
--And [Batch_Number] = '74661'
--Where Case When [Transaction] = 'CLOSSING_STOCK' Then RunningAgeTotal Else Quantity End = Quantity
Order By r1.[Stock Item],  r1.[Batch_Number], r1.[Consignment Stock], Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, [Transaction Time]
Go

------Select *
------From #Results3
--------Where RunningMatch != Quantity
------Order By unit, location, [Stock Item], [Batch_Number], UoM, Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, [Transaction Time]
------Go


