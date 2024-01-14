package xyz.kacperjanas.securityapi.tools;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.model.SecuritySystem;
import xyz.kacperjanas.securityapi.model.SystemStatus;
import xyz.kacperjanas.securityapi.repositories.SecuritySystemRepository;

@Component
public class DBInflator implements ApplicationListener<ContextRefreshedEvent> {
    private SecuritySystemRepository securitySystemRepository;

    public DBInflator(SecuritySystemRepository securitySystemRepository) {
        this.securitySystemRepository = securitySystemRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        SecuritySystem s1 = new SecuritySystem("Test System 1", "ABC", SystemStatus.UNLOCKED, false);
        SecuritySystem s2 = new SecuritySystem("Test System 2", "ABC", SystemStatus.LOCKED, true);

        securitySystemRepository.save(s1);
        securitySystemRepository.save(s2);
    }
}
