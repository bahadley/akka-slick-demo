package com.pcb.messages

case class CreateIndustry(in_id: String, in_name: String, in_sc_id: String)
case class DeleteIndustry(in_id: String)
case class CountIndustry(in_id: String)
