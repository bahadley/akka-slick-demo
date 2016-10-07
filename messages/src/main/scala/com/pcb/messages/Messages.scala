package com.pcb.messages

case class CreateIndustry(in_id: String, in_name: String, in_sc_id: String)
case class DeleteIndustry(in_id: String)
case class CountIndustry(in_id: String)

case class CreateStatusType(st_id: String, st_name: String)

case class CreateTaxRate(tx_id: String, tx_name: String, tx_rate: Double)

case class CreateTradeType(tt_id: String, tt_name: String, tt_is_sell: Int, tt_is_market: Int)
