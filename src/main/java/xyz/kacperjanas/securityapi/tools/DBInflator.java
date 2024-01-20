package xyz.kacperjanas.securityapi.tools;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.common.EEventType;
import xyz.kacperjanas.securityapi.model.AccessCard;
import xyz.kacperjanas.securityapi.model.SecurityEvent;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.common.ESystemStatus;
import xyz.kacperjanas.securityapi.repositories.AccessCardRepository;
import xyz.kacperjanas.securityapi.repositories.SecurityEventRepository;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

import java.util.Date;

@Component
public class DBInflator implements ApplicationListener<ContextRefreshedEvent> {
    private SecuritySystemRepository securitySystemRepository;
    private AccessCardRepository accessCardRepository;
    private SecurityEventRepository securityEventRepository;

    public DBInflator(SecuritySystemRepository securitySystemRepository, AccessCardRepository accessCardRepository, SecurityEventRepository securityEventRepository) {
        this.securitySystemRepository = securitySystemRepository;
        this.accessCardRepository = accessCardRepository;
        this.securityEventRepository = securityEventRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        SecuritySystem s1 = new SecuritySystem(
                "Test System 1",
                "ABC",
                ESystemStatus.UNLOCKED,
                false
        );

        AccessCard c1 = new AccessCard("TESTCARD1");
        SecurityEvent e1 = new SecurityEvent(EEventType.UNLOCK, new Date(1705751213000L));

        s1.getAccessCards().add(c1);
        e1.setSystem(s1);
        c1.getSystems().add(s1);


        SecuritySystem s2 = new SecuritySystem(
                "Test System 2",
                "ABC",
                ESystemStatus.LOCKED,
                true
        );

        AccessCard c2 = new AccessCard("TESTCARD2");
        SecurityEvent e2 = new SecurityEvent(EEventType.UNLOCK, new Date(1705752213000L));
        SecurityEvent e3 = new SecurityEvent(EEventType.LOCK, new Date(1705752813000L));

        s2.getAccessCards().add(c2);
        e2.setSystem(s2);
        e3.setSystem(s2);
        c2.getSystems().add(s2);

        securitySystemRepository.save(s1);
        securitySystemRepository.save(s2);

        accessCardRepository.save(c1);
        accessCardRepository.save(c2);

        securityEventRepository.save(e1);
        securityEventRepository.save(e2);
        securityEventRepository.save(e3);
    }
}
