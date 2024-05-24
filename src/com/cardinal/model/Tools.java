package com.cardinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tools {
 public String code;
 public String type;
 public String brand;
 
 public Tools(String code, String type, String brand){
	 this.code =code;
	 this.type = type;
	 this.brand = brand;
 }
}
