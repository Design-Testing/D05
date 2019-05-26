
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;
import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Actor extends DomainEntity {

	private String				name;
	private Collection<String>	surname;
	private String				photo;
	private String				email;
	private String				phone;
	private String				address;
	private Boolean				spammer;

	//Relational attributes
	private UserAccount			userAccount;


	@SafeHtml
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotEmpty
	@ElementCollection
	@EachNotBlank
	public Collection<String> getSurname() {
		return this.surname;
	}

	public void setSurname(final Collection<String> surname) {
		this.surname = surname;
	}

	@URL
	@SafeHtml
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	/*
	 * alias<identifier@domain> OR alias<identifier@> ^((([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+){0,1}>)$
	 * 
	 * identifier@domain OR identifier@ ^[\\w]+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}){0,1}$
	 */
	@NotBlank
	@Pattern(regexp = "^((([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+){0,1}>)$||^[\\w]+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}){0,1}$")
	@SafeHtml
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	// (regexp = "(^\\+([1-9]{1}[0-9]{1,2}))?[ ]*(\\([1-9]{1}[0-9]{1,2}\\))?[ ]*(\\d{4,}$)||''")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@SafeHtml
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(nullable = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Boolean getSpammer() {
		return this.spammer;
	}

	public void setSpammer(final Boolean spammer) {
		this.spammer = spammer;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
