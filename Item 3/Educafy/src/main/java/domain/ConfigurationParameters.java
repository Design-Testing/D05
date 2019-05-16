
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class ConfigurationParameters extends DomainEntity {

	private String				sysName;
	private String				banner;
	private String				welcomeMessageEsp;
	private String				welcomeMessageEn;
	private String				countryPhoneCode;
	private Collection<String>	positiveWords;
	private Collection<String>	negativeWords;
	private Collection<String>	spamWords;
	private int					maxFinderResults;
	private int					finderTime;
	private boolean				rebranding;
	private double				flatFare;
	private double				vat;

	private Collection<String>	creditCardMake;


	@NotBlank
	@SafeHtml
	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(final String sysName) {
		this.sysName = sysName;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageEsp() {
		return this.welcomeMessageEsp;
	}

	public void setWelcomeMessageEsp(final String welcomeMessageEsp) {
		this.welcomeMessageEsp = welcomeMessageEsp;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageEn() {
		return this.welcomeMessageEn;
	}

	public void setWelcomeMessageEn(final String welcomeMessageEn) {
		this.welcomeMessageEn = welcomeMessageEn;
	}

	//TODO
	@Pattern(regexp = "^[+][1-9]{0,2}")
	public String getCountryPhoneCode() {
		return this.countryPhoneCode;
	}

	public void setCountryPhoneCode(final String countryPhoneCode) {
		this.countryPhoneCode = countryPhoneCode;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@Range(min = 10, max = 100)
	public int getMaxFinderResults() {
		return this.maxFinderResults;
	}

	public void setMaxFinderResults(final int maxFinderResults) {
		this.maxFinderResults = maxFinderResults;
	}

	@Range(min = 1, max = 24)
	public int getFinderTime() {
		return this.finderTime;
	}

	public void setFinderTime(final int finderTime) {
		this.finderTime = finderTime;
	}

	@ElementCollection
	@EachNotBlank
	public Collection<String> getCreditCardMake() {
		return this.creditCardMake;
	}

	public void setCreditCardMake(final Collection<String> creditCardMake) {
		this.creditCardMake = creditCardMake;
	}

	public boolean isRebranding() {
		return this.rebranding;
	}

	public void setRebranding(final boolean rebranding) {
		this.rebranding = rebranding;
	}

	@Range(min = 0)
	public double getFlatFare() {
		return this.flatFare;
	}

	public void setFlatFare(final double flatFare) {
		this.flatFare = flatFare;
	}

	@Range(min = 0, max = 1)
	@Digits(integer = 1, fraction = 2)
	public double getVat() {
		return this.vat;
	}

	public void setVat(final double vat) {
		this.vat = vat;
	}

}
