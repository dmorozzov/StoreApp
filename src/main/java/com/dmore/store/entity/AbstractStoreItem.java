package com.dmore.store.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.PatternSyntaxException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ITEM")
public abstract class AbstractStoreItem implements Serializable {

	private static final long serialVersionUID = 2312831428292845026L;
	public static final String COLON_DELIMETER = ":";
	public static final String SPACE_DELIMETER = "     ";
	public static final String NEW_LINE_DELIMETER = System.getProperty("line.separator");
	
	private static final String BINDING_SERIAL_ID = "serial_id";
	private static final String BINDING_MANUFACTURER = "manufacturer";
	private static final String BINDING_PRICE = "price";
	private static final String BINDING_COUNT = "count";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = BINDING_SERIAL_ID)
	private long serialId;

	@Column(name = BINDING_MANUFACTURER)
	private String manufacturer;

	// for simplicity, double is not suitable for keeping money
	@Column(name = BINDING_PRICE)
	private double price;

	@Column(name = BINDING_COUNT)
	private int count;

	public AbstractStoreItem() {
	}

	public void incrementCount(int additionCount) {
		count += additionCount;
	}

	protected static class StoreItemBuilder {
		protected static Map<String, BiConsumer<String, AbstractStoreItem>> commonSetters;
		static {
			commonSetters = new HashMap<>();
			BiConsumer<String, AbstractStoreItem> serialIdSetter = (sId, item) -> item.setSerialId(Long.parseLong(sId));
			BiConsumer<String, AbstractStoreItem> manufacturerSetter = (manufacturer, item) -> item
					.setManufacturer(manufacturer);
			BiConsumer<String, AbstractStoreItem> priceSetter = (price, item) -> item
					.setPrice(Double.parseDouble(price));
			commonSetters.put(BINDING_SERIAL_ID, serialIdSetter);
			commonSetters.put(BINDING_MANUFACTURER, manufacturerSetter);
			commonSetters.put(BINDING_PRICE, priceSetter);
		}

		public StoreItemBuilder parceSpecific(List<String> specifics) {
			for (String pair : specifics) {
				try {
					String[] paramValue = pair.split(COLON_DELIMETER);
					setSpecific(paramValue[0], paramValue[1]);
				} catch (PatternSyntaxException patternSyntaxException) {
					continue;
				} catch (Exception exception) {
					System.out.println(exception.getMessage());
					continue;
				}
			}
			return this;
		}

		public void setSpecific(String param, String value) {
		}

		public AbstractStoreItem build() {
			return null;
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSerialId() {
		return serialId;
	}

	public void setSerialId(long serialId) {
		this.serialId = serialId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public abstract String getTypeLabel();

	@Override
	public String toString() {
		String presentation = new StringBuilder()
				.append(BINDING_SERIAL_ID).append(COLON_DELIMETER).append(serialId).append(NEW_LINE_DELIMETER)
				.append(SPACE_DELIMETER).append(BINDING_COUNT).append(COLON_DELIMETER).append(count).append(NEW_LINE_DELIMETER)
				.append(SPACE_DELIMETER).append(BINDING_MANUFACTURER).append(COLON_DELIMETER).append(manufacturer).append(NEW_LINE_DELIMETER)
				.append(SPACE_DELIMETER).append(BINDING_PRICE).append(COLON_DELIMETER).append(price).append(NEW_LINE_DELIMETER)		
				.toString();
		
		return presentation;
	}
}
