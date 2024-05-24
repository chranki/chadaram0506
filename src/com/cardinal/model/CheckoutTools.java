package com.cardinal.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutTools {

	public String toolCode;
	public String toolType;
	public String toolbrand;
	public int rentalDays;
	public Date checkoutDate;
	public Date dueDate;
	public BigDecimal dailyRentalCharge;
	public int chargeDays;
	public int prediscountCharge;
	public int discountCharge;
	public int discountAmount;
	public BigDecimal finalCharge;
	
}
