package com.springboot.electronicstore.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

// Lombok annotations to generate getters, setters, no-args constructor, all-args constructor, and builder pattern methods
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// JPA annotation to specify this class is an entity and is mapped to a database table
@Entity
// JPA annotation to define the table name in the database for this entity
@Table(name = "user")
public class User {

	// JPA annotation to specify the primary key of the entity
	@Id
	// JPA annotation to define the column name in the database for this field
	@Column(name = "user_id")
	private String userId;

	// JPA annotation to define the column name in the database for this field
	@Column(name = "user_name")
	private String userName;

	// JPA annotation to define the column name in the database for this field
	@Column(name = "user_email")
	private String userEmail;

	// JPA annotation to define the column name in the database for this field and
	// set a column length of 8 characters
	@Column(name = "user_password", length = 500)
	private String userPassword;

	// JPA annotation to define the column name in the database for this field
	@Column(name = "user_gender")
	private String userGender;

	// JPA annotation to define the column name in the database for this field
	@Column(name = "user_image")
	private String userImage;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Order> orders = new ArrayList<>();

}
