
package forms;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class ActorForm extends DomainEntity {

	private String				name;
	private Collection<String>	surname;
	private String				photo;
	private String				email;
	private String				phone;
	private String				address;
	private double				vat;

	private Boolean				termsAndCondicions;


	@AssertTrue
	public Boolean getTermsAndCondicions() {
		return this.termsAndCondicions;
	}

	public void setTermsAndCondicions(final Boolean termsAndCondicions) {
		this.termsAndCondicions = termsAndCondicions;
	}


	//Relational attributes
	private String	userAccountuser;
	private String	userAccountpassword;


	@Size(min = 5, max = 32)
	public String getUserAccountuser() {
		return this.userAccountuser;
	}

	public void setUserAccountuser(final String userAccountuser) {
		this.userAccountuser = userAccountuser;
	}
	@Size(min = 5, max = 32)
	public String getUserAccountpassword() {
		return this.userAccountpassword;
	}

	public void setUserAccountpassword(final String userAccountpassword) {
		this.userAccountpassword = userAccountpassword;
	}

	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotEmpty
	@EachNotBlank
	@ElementCollection
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

	@NotBlank
	@Pattern(regexp = "^((([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+){0,1}>)$||^[\\w]+@((?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}){0,1}$")
	@SafeHtml
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	// regexp = "((^\\+([1-9]{1,3}))?[ -]*(6|7)[ -]*([0-9][ -]*){8})||''"
	// @Pattern(regexp = "(^\\+([1-9]{1}[0-9]{1,2}))?[ ]*(\\([1-9]{1}[0-9]{1,2}\\))?[ ]*(\\d{4,}$)||''")
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

	//	@Range(min = (long) -1.00, max = (long) 1.00)
	//	public Double getScore() {
	//		return this.score;
	//	}
	//
	//	public void setScore(final Double score) {
	//		this.score = score;
	//	}

	//	public Boolean getSpammer() {
	//		return this.spammer;
	//	}
	//
	//	public void setSpammer(final Boolean spammer) {
	//		this.spammer = spammer;
	//	}

	@NotNull
	@Range(min = 0, max = 0)
	@Digits(integer = 1, fraction = 2)
	public double getVat() {
		return this.vat;
	}

	public void setVat(final double vat) {
		this.vat = vat;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
