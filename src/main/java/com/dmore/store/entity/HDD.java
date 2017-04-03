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
@Table(name = "HDD")
public class HDD extends AbstractStoreItem {

	private static final long serialVersionUID = 5674408177813162571L;

	private static final String BINDING_HDD_CAPACITY = "hdd_capacity";

	@Column(name = BINDING_HDD_CAPACITY)
	private long capacity;

	public HDD() {
	}
	
	public static class HddBuilder extends StoreItemBuilder {
		private HDD hddTemplate;
		private static Map<String, BiConsumer<String, ? super HDD>> hddSetters;
		
		static {
			hddSetters = new HashMap<>();
			hddSetters.putAll(commonSetters);
			hddSetters.put(BINDING_HDD_CAPACITY, (capacity, item) -> item.setCapacity(Long.parseLong(capacity)));
		}
		
		public HddBuilder(int count) {
			hddTemplate = new HDD();
			hddTemplate.setCount(count);
		}
		
		@Override
		public void setSpecific(String param, String value) {
			BiConsumer<String, ? super HDD> consumer = hddSetters.get(param);
			if (consumer != null) {
				consumer.accept(value, hddTemplate);
			}
		};
		
		public AbstractStoreItem build() {
			return hddTemplate;
		}
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	@Override
	public String getTypeLabel() {
		return "HDDs";
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		String presentation = stringBuilder.append(SPACE_DELIMETER).append(BINDING_HDD_CAPACITY)
										.append(COLON_DELIMETER).append(capacity).append(NEW_LINE_DELIMETER).toString();
		return presentation;
	}
	
}
