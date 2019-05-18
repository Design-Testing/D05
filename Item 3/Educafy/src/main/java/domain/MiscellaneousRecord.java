
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class MiscellaneousRecord extends DomainEntity {

	private String				freeText;
	private Collection<String>	attachments;
	private boolean				isCertified;
	private boolean				isDraft;


	@NotBlank
	@SafeHtml
	public String getFreeText() {
		return this.freeText;
	}

	public void setFreeText(final String freeText) {
		this.freeText = freeText;
	}

	@NotNull
	@EachURL
	@ElementCollection
	public Collection<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final Collection<String> attachments) {
		this.attachments = attachments;
	}

	@NotNull
	public boolean isCertified() {
		return this.isCertified;
	}

	public void setCertified(final boolean isCertified) {
		this.isCertified = isCertified;
	}

	@NotNull
	public boolean isDraft() {
		return this.isDraft;
	}

	public void setDraft(final boolean isDraft) {
		this.isDraft = isDraft;
	}

}
