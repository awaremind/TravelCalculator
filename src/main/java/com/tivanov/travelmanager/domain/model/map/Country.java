package com.tivanov.travelmanager.domain.model.map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity()
@NoArgsConstructor
public class Country {
	
	@Id
	private long id;
    
	@Getter @Setter
	private String name;
	
	@NotBlank
	@Getter @Setter
	private String code;
	
	@Getter @Setter
	private String currency;
	
	public Country(String code) {
		super();
		this.code = code;
	}
	
	public Country(String name, @NotBlank String code, String currency) {
		super();
		this.name = name;
		this.code = code;
		this.currency = currency;
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
