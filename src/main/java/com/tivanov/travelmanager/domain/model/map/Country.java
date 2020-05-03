package com.tivanov.travelmanager.domain.model.map;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.Setter;

@Entity()
public class Country {
	
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long id;
    
	@Getter @Setter
	@Column
	private String name;
	
	@NotBlank
	@Getter @Setter
	@Column
	private String code;
	
	@Getter @Setter
	@Column
	private String currency;
	
	@Getter @Setter
	@Column
	private BigDecimal rate;
	
//	@OneToMany(mappedBy = "country", 
//			cascade = CascadeType.ALL, 
//			fetch = FetchType.EAGER)
//	private List<String> neighbours = new ArrayList<>();
	
	public Country(String code, String name) {
		super();
		this.name = name;
		this.code = code;
	}

	public Country(String code) {
		super();
		this.code = code;
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
