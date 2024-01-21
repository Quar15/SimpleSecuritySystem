package xyz.kacperjanas.securityapi.converters;

import jakarta.annotation.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.commands.AccessCardCommand;
import xyz.kacperjanas.securityapi.commands.SecuritySystemCommand;
import xyz.kacperjanas.securityapi.model.AccessCard;
import xyz.kacperjanas.securityapi.model.SecuritySystem;

@Component
public class AccessCardToAccessCardCommand implements Converter<AccessCard, AccessCardCommand> {
    @Synchronized
    @Nullable
    @Override
    public AccessCardCommand convert(AccessCard source) {
        final AccessCardCommand accessCardCommand = new AccessCardCommand();
        accessCardCommand.setId(source.getId());
        accessCardCommand.setCardValue(source.getCardValue());
        accessCardCommand.setCreatedAt(source.getCreatedAt());
        accessCardCommand.setUpdatedAt(source.getUpdatedAt());

        return accessCardCommand;
    }
}
