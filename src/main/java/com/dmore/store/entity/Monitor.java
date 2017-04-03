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
@Table(name = "MONITOR")
public class Monitor extends AbstractStoreItem {

	private static final long serialVersionUID = 7092122255382400037L;

	private static final String BINDING_MONITOR_DIAGONAL = "monitor_diagonal";

	@Column(name = BINDING_MONITOR_DIAGONAL)
	private float diagonal;

	public Monitor() {
	}

	public static class MonitorBuilder extends StoreItemBuilder {
		private Monitor monitorTemplate;
		private static Map<String, BiConsumer<String, ? super Monitor>> monitorSetters;

		static {
			monitorSetters = new HashMap<>();
			monitorSetters.putAll(commonSetters);
			monitorSetters.put(BINDING_MONITOR_DIAGONAL, (size, item) -> item.setDiagonal(Float.parseFloat(size)));
		}

		public MonitorBuilder(int count) {
			monitorTemplate = new Monitor();
			monitorTemplate.setCount(count);
		}

		@Override
		public void setSpecific(String param, String value) {
			BiConsumer<String, ? super Monitor> consumer = monitorSetters.get(param);
			if (consumer != null) {
				consumer.accept(value, monitorTemplate);
			}
		};

		public AbstractStoreItem build() {
			return monitorTemplate;
		}
	}

	public float getDiagonal() {
		return diagonal;
	}

	public void setDiagonal(float diagonal) {
		this.diagonal = diagonal;
	}

	@Override
	public String getTypeLabel() {
		return "Monitors";
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		String presentation = stringBuilder.append(SPACE_DELIMETER).append(BINDING_MONITOR_DIAGONAL)
										.append(COLON_DELIMETER).append(diagonal).append(NEW_LINE_DELIMETER).toString();
		return presentation;
	}
	
}
