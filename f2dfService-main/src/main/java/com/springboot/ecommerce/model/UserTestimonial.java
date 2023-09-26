package com.springboot.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*this is Testimonial type*/
@Entity
@Table(name = "UserTestimonial")
public class UserTestimonial {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "img")
	private String img;

	@Column(name = "description")
	private String desc;

	@Column(name = "degination")
	private String degination;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDegination() {
		return degination;
	}

	public void setDegination(String degination) {
		this.degination = degination;
	}

	@Override
	public String toString() {
		return "Testimonial [id=" + id + ", name=" + name + ", img=" + img + ", desc=" + desc + ", degination="
				+ degination + "]";
	}

}
