package com.tivanov.travelmanager.domain.model.map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Country{
    
	@NotEmpty(message = "Country name is mandatory") 
	@Getter @Setter
	private String name;
	
	@NotBlank(message = "Country code is mandatory") 
	@Getter
	private String code;
	
	@NotEmpty(message = "Country currency code is mandatory") 
	@Getter 
	private String currency;
	
	public Country(String code) {
		super();
		this.code = code.toUpperCase();
	}
	
	public Country(String name, @NotBlank String code, String currency) {
		super();
		this.name = name;
		this.code = code.toUpperCase();
		this.currency = currency.toUpperCase();
	}

	public Country(Country another) {
		this.code = new String(another.getCode());
		this.currency = new String(another.getCurrency());
		this.name = new String(another.getName());
	}

	public void setCode(String code) {
		this.code = code.toUpperCase();
	}

	public void setCurrency(String currency) {
		this.currency = currency.toUpperCase();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(code)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else { 
			Country other = (Country) obj;
			return new EqualsBuilder()
					.append(this.code, other.code)
					.isEquals();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("code:").append(this.code + ",\n");
		sb.append("name:").append(this.name + ",\n");
		sb.append("currency:").append(this.currency + ",\n");
		
		return sb.toString();
	}

}
