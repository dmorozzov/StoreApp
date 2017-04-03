package com.dmore.store.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "LAPTOP")
public class Laptop extends AbstractStoreItem {
	private static final long serialVersionUID = 4522117490175811741L;
	private static final String BINDING_LAPTOP_SIZE = "laptop_size";
	
	@Column(name = BINDING_LAPTOP_SIZE)
	private int size;

	public Laptop() {
	}
	
	public static class LaptopBuilder extends StoreItemBuilder {
		private Laptop laptopTemplate;
		private static Map<String, BiConsumer<String, ? super Laptop>> laptopSetters;
		
		static {
			laptopSetters = new HashMap<>();
			laptopSetters.putAll(commonSetters);
			laptopSetters.put(BINDING_LAPTOP_SIZE, (size, item) -> item.setSize(Integer.parseInt(size)));
		}
		
		public LaptopBuilder(int count) {
			laptopTemplate = new Laptop();
			laptopTemplate.setCount(count);
		}
		
		@Override
		public void setSpecific(String param, String value) {
			BiConsumer<String, ? super Laptop> consumer = laptopSetters.get(param);
			if (consumer != null) {
				consumer.accept(value, laptopTemplate);
			}
		};
		
		public AbstractStoreItem build() {
			return laptopTemplate;
		}
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public String getTypeLabel() {
		return "Laptops";
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		String presentation = stringBuilder.append(SPACE_DELIMETER).append(BINDING_LAPTOP_SIZE)
										.append(COLON_DELIMETER).append(size).append(NEW_LINE_DELIMETER).toString();
		return presentation;
	}
	
}
