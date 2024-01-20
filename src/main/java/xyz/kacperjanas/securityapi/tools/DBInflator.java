package xyz.kacperjanas.securityapi.tools;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.model.AccessCard;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.common.ESystemStatus;
import xyz.kacperjanas.securityapi.repositories.AccessCardRepository;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

@Component
public class DBInflator implements ApplicationListener<ContextRefreshedEvent> {
    private SecuritySystemRepository securitySystemRepository;
    private AccessCardRepository accessCardRepository;

    public DBInflator(SecuritySystemRepository securitySystemRepository, AccessCardRepository accessCardRepository) {
        this.securitySystemRepository = securitySystemRepository;
        this.accessCardRepository = accessCardRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        SecuritySystem s1 = new SecuritySystem("Test System 1", "ABC", ESystemStatus.UNLOCKED, false);
        SecuritySystem s2 = new SecuritySystem("Test System 2", "ABC", ESystemStatus.LOCKED, true);

        AccessCard c1 = new AccessCard("TESTCARD1");
        AccessCard c2 = new AccessCard("TESTCARD2");

        s1.getAccessCards().add(c1);
        s2.getAccessCards().add(c2);

        c1.getSystems().add(s1);
        c2.getSystems().add(s2);

        securitySystemRepository.save(s1);
        securitySystemRepository.save(s2);

        accessCardRepository.save(c1);
        accessCardRepository.save(c2);
    }
}
