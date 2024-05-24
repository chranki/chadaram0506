package com.cardinal.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.cardinal.model.CheckoutTools;
import com.cardinal.model.ToolCharge;
import com.cardinal.model.Tools;
  

public class CheckoutService {

	public static void main(String[] args) {
		Tools chainsawTools = new Tools("CHNS", "Chainsaw", "Stihl");
		Tools LadderTools = new Tools("LADW", "Ladder", "Werner");
		Tools JackhammerJakdTools = new Tools("JAKD", "Jackhammer", "DeWalt");
		Tools JackhammerJakRTools = new Tools("JAKR", "Jackhammer", "Ridgid");

		ArrayList<Tools> toolList = new ArrayList<Tools>();
		toolList.add(chainsawTools);
		toolList.add(LadderTools);
		toolList.add(JackhammerJakdTools);
		toolList.add(JackhammerJakRTools);

		ToolCharge ladderToolCharge = new ToolCharge("Ladder", new BigDecimal(1.99), true, true, false);
		ToolCharge chainshawToolCharge = new ToolCharge("Chainsaw", new BigDecimal(1.49), true, false, true);
		ToolCharge jackhammerToolCharge = new ToolCharge("Jackhammer", new BigDecimal(2.99), true, false, false);

		ArrayList<ToolCharge> toolChargeList = new ArrayList<ToolCharge>();
		toolChargeList.add(ladderToolCharge);
		toolChargeList.add(chainshawToolCharge);
		toolChargeList.add(jackhammerToolCharge);
		
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Enter Tool Code values as CHNS,LADW, JAKD and JAKR:");
        String toolCode = myObj.nextLine();
        
        System.out.println("Enter Rental Days:");
        int rentalDays = Integer.valueOf(myObj.nextLine());
        
        // logic to validate rental days
        if(rentalDays <0){
        	System.out.println("Rental day count is not 1 or greater");
        	System.exit(0);
        }
        
        System.out.println("Enter Check out Date:");
        String checkoutDate = myObj.nextLine();
        
        System.out.println("Enter Discount Percent:");
        int discountPercent = Integer.valueOf(myObj.nextLine());
        
        BigDecimal actualDiscount = new BigDecimal(discountPercent);
        

		CheckoutTools checkoutTools = new CheckoutTools();
		checkoutTools.setToolCode(toolCode);
		checkoutTools.setRentalDays(rentalDays);
		String dateInString = checkoutDate; //"05/24/24";
		
		// logic to determine Due date based on checkout date and rental days.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

		LocalDate localDate = LocalDate.parse(dateInString, formatter);
		LocalDate newDueDate = localDate.plusDays(checkoutTools.getRentalDays());

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uu");
		String textDueDate = newDueDate.format(formatters);
		
		DateTimeFormatter formattersCheckoutDate = DateTimeFormatter.ofPattern("d/MM/uu");
		String textCheckoutDate = localDate.format(formattersCheckoutDate);
		
		// logic define Date on weekday or weekend
		
		long weekdays= localDate.datesUntil(newDueDate)
		      .map(LocalDate::getDayOfWeek)
		      .filter(day -> !Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day))
		      .count();
		
		
        
		long weekends= localDate.datesUntil(newDueDate)
			      .map(LocalDate::getDayOfWeek)
			      .filter(day -> Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day))
			      .count();
		
		// logic to do final charge
		 MathContext m = new MathContext(4);
		 
		 String toolType = ladderToolCharge.getToolType();
		 
		 BigDecimal finalChargeWithoutDiscountForWeekDays = new BigDecimal(0);
		 
		 BigDecimal finalChargeWithoutDiscountForWeekEnd = new BigDecimal(0);
		 
		 switch(toolType){
			 case "Chainsaw":
				 finalChargeWithoutDiscountForWeekDays = BigDecimal.valueOf(weekdays).multiply(getToolCharging(ladderToolCharge.getToolType(), toolChargeList),m);
				 finalChargeWithoutDiscountForWeekEnd = BigDecimal.valueOf(weekends).multiply(getToolCharging(ladderToolCharge.getToolType(), toolChargeList),m);
				 break;
			 case "Ladder":
				 finalChargeWithoutDiscountForWeekDays = BigDecimal.valueOf(weekdays).multiply(getToolCharging(ladderToolCharge.getToolType(), toolChargeList),m);
				 break;
			 case "Jackhammer":
				 finalChargeWithoutDiscountForWeekDays = BigDecimal.valueOf(weekdays).multiply(getToolCharging(ladderToolCharge.getToolType(), toolChargeList),m);
				 break;
			 default:
				 System.out.println("No Matching Tool Type found.");
		 }

		 BigDecimal finalChargeWithoutDiscountDays = finalChargeWithoutDiscountForWeekDays.add(finalChargeWithoutDiscountForWeekEnd);
		 BigDecimal finalChargeWithDiscount = (finalChargeWithoutDiscountDays.multiply(actualDiscount)).divide(new BigDecimal(100));
		 BigDecimal finalCharge = finalChargeWithoutDiscountDays.subtract(finalChargeWithDiscount);
        
		
		printResponse(toolList, ladderToolCharge, discountPercent, checkoutTools, textDueDate, textCheckoutDate,
				weekdays, weekends, finalChargeWithoutDiscountDays, finalChargeWithDiscount, finalCharge);

	}

	private static void printResponse(ArrayList<Tools> toolList, ToolCharge ladderToolCharge, int discountPercent,
			CheckoutTools checkoutTools, String textDueDate, String textCheckoutDate, long weekdays, long weekends,
			BigDecimal finalChargeWithoutDiscountDays, BigDecimal finalChargeWithDiscount, BigDecimal finalCharge) {
		System.out.println("######################## START ##########################");
		System.out.println("Tool Code :" + toolCodeSelected(ladderToolCharge.getToolType(), toolList));
		System.out.println("Tool Type :" + ladderToolCharge.getToolType());
		System.out.println("Checkout Date :" + textCheckoutDate);
		System.out.println("Rental Days :" + checkoutTools.getRentalDays());
		System.out.println("Due Date :" + textDueDate);
		System.out.println(" No of Week Days are :"+weekdays);
		System.out.println(" No of Week Ends are :"+weekends);
		System.out.println("Percent :"+discountPercent);
		System.out.println("Final Charge With out Discount :$"+finalChargeWithoutDiscountDays.toPlainString());
		System.out.println("Discount Price :$"+finalChargeWithDiscount.toPlainString());
		System.out.println("Final Charge  :$"+finalCharge.toPlainString());
		System.out.println("######################### END ###########################");
	}

	public static String toolCodeSelected(String toolType, ArrayList<Tools> toollist) {
		String toolCode = null;
		for (Tools tools : toollist) {
			if (tools.getType().equals(toolType)) {
				toolCode = tools.getCode();
				continue;
			}
			
		}
		return toolCode;
	}
	
	public static BigDecimal getToolCharging(String toolType, ArrayList<ToolCharge> toolChargeList) {
		BigDecimal toolChargeValue = null;
		for (ToolCharge toolCharge : toolChargeList) {
			if (toolCharge.getToolType().equals(toolType)) {
				toolChargeValue = toolCharge.getDailyCharge();
				continue;
			}
		}
		return toolChargeValue;
	}
}
