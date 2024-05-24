package com.cardinal.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ToolCharge{
	
    public String toolType;
	public BigDecimal dailyCharge;
	 public boolean weekdayCharge;
	 public boolean weekendCharge;
	 public boolean holidayCharge;

	 public ToolCharge(String toolType, BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge){
		 this.toolType = toolType;
		 this.dailyCharge = dailyCharge;
		 this.weekdayCharge = weekdayCharge;
		 this.weekendCharge = weekendCharge;
		 this.holidayCharge = holidayCharge;
		 
	 }
}
