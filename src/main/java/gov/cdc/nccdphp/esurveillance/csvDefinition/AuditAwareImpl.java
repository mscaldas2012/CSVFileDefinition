package gov.cdc.nccdphp.esurveillance.csvDefinition;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("<<CHANGE_ME>>");
	}

}
