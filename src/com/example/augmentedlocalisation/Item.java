package com.example.augmentedlocalisation;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("Item")
public class Item extends IBMDataObject {
	public static final String CLASS_NAME = "Item";
	private static final String NAME = "name";
	public String getName() {
		return (String) getObject(NAME);
	}
	public void setName(String itemName) {
		setObject(NAME, (itemName != null) ? itemName : "");
	}

}
