package com.dmore.store.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "COMPUTER")
public class Computer extends AbstractStoreItem {

	private static final long serialVersionUID = -8249060591073999987L;
	private static final String BINDING_COMPUTER_TYPE = "computer_type";
	
	@Enumerated(EnumType.STRING)
	@Column(name = BINDING_COMPUTER_TYPE)
	private ComputerType computerType;

	public Computer() {
	}
	
	public static class ComputerBuilder extends StoreItemBuilder {
		private Computer computerTemplate;
		private static Map<String, BiConsumer<String, ? super Computer>> computerSetters;
		
		static {
			computerSetters = new HashMap<>();
			computerSetters.putAll(commonSetters);
			computerSetters.put(BINDING_COMPUTER_TYPE, (type, item) -> {
				try {
					ComputerType computerType = ComputerType.valueOf(type.toUpperCase(Locale.ENGLISH));
					item.setComputerType(computerType);
				} catch(Exception ex) {
					return;
				}
			});
		}
		
		public ComputerBuilder(int count) {
			computerTemplate = new Computer();
			computerTemplate.setCount(count);
		}
		
		@Override
		public void setSpecific(String param, String value) {
			BiConsumer<String, ? super Computer> consumer = computerSetters.get(param);
			if (consumer != null) {
				consumer.accept(value, computerTemplate);
			}
		};
		
		public AbstractStoreItem build() {
			return computerTemplate;
		}
	}
	
	public String getNotNullComputerType() {
		return computerType != null ? computerType.name() : "";
	}

	public ComputerType getComputerType() {
		return computerType;
	}

	public void setComputerType(ComputerType computerType) {
		this.computerType = computerType;
	}

	@Override
	public String getTypeLabel() {
		return "Computers";
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		String presentation = stringBuilder.append(SPACE_DELIMETER).append(BINDING_COMPUTER_TYPE)
										.append(COLON_DELIMETER).append(getNotNullComputerType()).append(NEW_LINE_DELIMETER).toString();
		return presentation;
	}
}
