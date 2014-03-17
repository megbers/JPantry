package org.egbers.homeautomation.kitchen.upc.domain;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.ALWAYS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Entity
@Table(name = "item", catalog = "jpantry")
@JsonSerialize(include = ALWAYS)
public class Item {
	private Long id;
	private String upc;
	private String name;
	private String description;
	private Integer quantity;
	private Boolean onList;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "upc")
	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "on_list")
	public Boolean getOnList() {
		return onList;
	}

	public void setOnList(Boolean onList) {
		this.onList = onList;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
		.append("ID: ")
		.append(id)
		.append("| UPC:")
		.append(upc)
		.append("| Name: ")
		.append(name)
		.append("| Description: ")
		.append(description)
		.append("| Quantity: ")
		.append(quantity)
		.append("| On List: ")
		.append(onList);
		
		return builder.toString();
	}
}
