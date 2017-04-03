package com.dmore.store.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StoreItemFactory {

	private static final String SEMICOLON_DELIMETER = ";";
	private static final String COMPUTER_ITEM_TYPE = "computer";
	private static final String LAPTOP_ITEM_TYPE = "laptop";
	private static final String MONITOR_ITEM_TYPE = "monitor";
	private static final String HDD_ITEM_TYPE = "hdd";

	public static AbstractStoreItem createFromString(String line) {
		if (line == null || line.isEmpty()) {
			return null;
		}
		String[] parts = line.split(SEMICOLON_DELIMETER);
		if (parts.length < 2) {
			return null;
		}
		int count = Integer.parseInt(parts[1]);
		List<String> specificParams = new ArrayList<>();
		for (int i = 2; i < parts.length; i++) {
			specificParams.add(parts[i]);
		}
		switch (parts[0]) {
		case COMPUTER_ITEM_TYPE:
			return new Computer.ComputerBuilder(count).parceSpecific(specificParams).build();
		case LAPTOP_ITEM_TYPE:
			return new Laptop.LaptopBuilder(count).parceSpecific(specificParams).build();
		case MONITOR_ITEM_TYPE:
			return new Monitor.MonitorBuilder(count).parceSpecific(specificParams).build();
		case HDD_ITEM_TYPE:
			return new HDD.HddBuilder(count).parceSpecific(specificParams).build();
		default:
			return null;
		}
	}
}
