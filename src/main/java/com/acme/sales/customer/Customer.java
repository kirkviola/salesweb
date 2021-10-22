package com.acme.sales.customer;

import javax.persistence.*;

@Entity(name="customers")
@Table(uniqueConstraints=@UniqueConstraint(name="UIDX_code", columnNames= {"code"}))

public class Customer {
	@Id
	@GeneratedValue(
			strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=30, nullable=false)
	private String code;
	@Column(length=30, nullable=false)
	private String name;
	private double sales;
	
	public Customer() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSales() {
		return sales;
	}
	public void setSales(double sales) {
		this.sales = sales;
	}
	
	
}
