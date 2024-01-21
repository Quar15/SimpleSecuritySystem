package xyz.kacperjanas.securityapi.converters;

import jakarta.annotation.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.commands.SecuritySystemCommand;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

@Component
public class SecuritySystemCommandToSecuritySystem implements Converter<SecuritySystemCommand, SecuritySystem> {

    @Synchronized
    @Nullable
    @Override
    public SecuritySystem convert(SecuritySystemCommand source) {
        final SecuritySystem securitySystem = new SecuritySystem();
        securitySystem.setId(source.getId());
        securitySystem.setPrettyName(source.getPrettyName());
        securitySystem.setMacAddress(source.getMacAddress());
        securitySystem.setStatus(source.getStatus());
        securitySystem.setCreatedAt(source.getCreatedAt());
        securitySystem.setUpdatedAt(source.getUpdatedAt());
        securitySystem.setFavourite(source.getFavourite());

        securitySystem.setEvents(source.getEvents());

        return securitySystem;
    }
}
