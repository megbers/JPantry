package org.egbers.homeautomation.kitchen.upc.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "expense", catalog = "jpantry")
public class Item {
	private Long id;
	private String upc;
	private String name;
	private String description;
}
