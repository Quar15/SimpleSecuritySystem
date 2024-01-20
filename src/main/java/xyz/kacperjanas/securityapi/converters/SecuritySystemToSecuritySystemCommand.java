package xyz.kacperjanas.securityapi.converters;

import jakarta.annotation.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.commands.SecuritySystemCommand;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

@Component
public class SecuritySystemToSecuritySystemCommand implements Converter<SecuritySystem, SecuritySystemCommand> {

    @Synchronized
    @Nullable
    @Override
    public SecuritySystemCommand convert(SecuritySystem source) {
        final SecuritySystemCommand securitySystemCommand = new SecuritySystemCommand();
        securitySystemCommand.setId(source.getId());
        securitySystemCommand.setPrettyName(source.getPrettyName());
        securitySystemCommand.setMacAddress(source.getMacAddress());
        securitySystemCommand.setStatus(source.getStatus());
        securitySystemCommand.setCreatedAt(source.getCreatedAt());
        securitySystemCommand.setUpdatedAt(source.getUpdatedAt());
        securitySystemCommand.setFavourite(source.getFavourite());

        securitySystemCommand.setEvents(source.getEvents());

        return securitySystemCommand;
    }
}
