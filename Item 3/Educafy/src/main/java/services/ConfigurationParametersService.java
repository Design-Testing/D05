
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigurationParametersRepository;
import security.Authority;
import domain.Actor;
import domain.Administrator;
import domain.ConfigurationParameters;

@Service
@Transactional
public class ConfigurationParametersService {

	@Autowired
	private ConfigurationParametersRepository	configurationParametersRepository;

	@Autowired
	private AdministratorService				administratorService;

	@Autowired
	private ActorService						actorService;

	@Autowired
	private MessageService						messageService;


	public ConfigurationParameters create() {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		return new ConfigurationParameters();
	}

	public Collection<ConfigurationParameters> findAll() {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		final Collection<ConfigurationParameters> res = this.configurationParametersRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public ConfigurationParameters findOne(final int id) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(id != 0);
		final ConfigurationParameters res = this.configurationParametersRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public boolean checkForSpamWords(final Collection<String> strings) {
		boolean res = false;
		final Collection<String> spamWords = this.findSpamWords();

		for (final String s : strings)
			for (final String spamWord : spamWords) {
				final boolean bool = s.matches(".*" + spamWord + ".*");
				// if (s.contentEquals(spamWord)) {
				if (bool) {
					res = true;
					break;
				}
			}
		return res;
	}

	//======================================================================================================
	//======================================================================================================
	//======================================================================================================
	//================================  Ancilliary methods: a8081  =========================================
	//======================================================================================================
	//======================================================================================================
	//======================================================================================================

	public Collection<String> findSpamWords() {
		final Collection<String> res = this.configurationParametersRepository.findSpamWords();
		Assert.notNull(res);
		return res;
	}

	public String findWelcomeMessageEsp() {
		return this.configurationParametersRepository.findWelcomeMessageEsp();
	}

	public String findWelcomeMessageEn() {
		return this.configurationParametersRepository.findWelcomeMessageEn();
	}

	public String findBanner() {
		return this.configurationParametersRepository.findBanner();
	}

	public String findSysName() {
		return this.configurationParametersRepository.findSysName();
	}

	public Collection<String> findSubjectLevels() {
		return this.configurationParametersRepository.findSubjectLevels();
	}

	public void rebranding(final String newSysName) {
		this.administratorService.findByPrincipal();
		Assert.notNull(newSysName);
		Assert.isTrue(!newSysName.isEmpty());
		final ConfigurationParameters cfg = this.find();
		Assert.isTrue(!cfg.isRebranding());
		final String sysName = this.findSysName();
		String welEn = cfg.getWelcomeMessageEn();
		String welEs = cfg.getWelcomeMessageEsp();
		welEn = welEn.replace(sysName, newSysName);
		welEs = welEs.replace(sysName, newSysName);
		cfg.setWelcomeMessageEn(welEn);
		cfg.setWelcomeMessageEsp(welEs);
		cfg.setSysName(newSysName);
		cfg.setRebranding(true);
		this.save(cfg);
		this.messageService.rebrandNotification(sysName);
	}

	public ConfigurationParameters find() {
		final ConfigurationParameters res = (ConfigurationParameters) this.configurationParametersRepository.findAll().toArray()[0];
		Assert.notNull(res);
		return res;
	}

	public ConfigurationParameters save(final ConfigurationParameters c) {
		Assert.notNull(c);
		final Administrator a = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(a, Authority.ADMIN);
		Assert.isTrue(isAdmin);

		final ConfigurationParameters result = this.configurationParametersRepository.save(c);
		Assert.notNull(result);
		return result;

	}
	// Ancilliary methods

	public String createTicker() {
		final DateFormat df = new SimpleDateFormat("yymmdd");
		final Calendar cal = Calendar.getInstance();

		return df.format(cal.getTime()) + "-" + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
	}

	public Collection<String> addSpamWord(final String sword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(sword);
		final Collection<String> swords = this.findSpamWords();

		return this.addWord(sword, swords);
	}

	public Collection<String> deleteSpamWord(final String sword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(sword);
		final Collection<String> swords = this.findSpamWords();

		return this.deleteWord(sword, swords);
	}

	private Collection<String> addWord(final String word, final Collection<String> words) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(!words.contains(word));
		words.add(word);

		return words;
	}

	private Collection<String> deleteWord(final String word, final Collection<String> words) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(words.contains(word));
		words.remove(word);

		return words;
	}

	public void flush() {
		this.configurationParametersRepository.flush();
	}

}
