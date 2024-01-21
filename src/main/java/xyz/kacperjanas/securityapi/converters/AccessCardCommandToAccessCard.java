package xyz.kacperjanas.securityapi.converters;

import jakarta.annotation.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import xyz.kacperjanas.securityapi.commands.AccessCardCommand;
import xyz.kacperjanas.securityapi.model.AccessCard;

@Component
public class AccessCardCommandToAccessCard implements Converter<AccessCardCommand, AccessCard> {
    @Synchronized
    @Nullable
    @Override
    public AccessCard convert(AccessCardCommand source) {
        final AccessCard accessCardCommand = new AccessCard();
        accessCardCommand.setId(source.getId());
        accessCardCommand.setCardValue(source.getCardValue());
        accessCardCommand.setCreatedAt(source.getCreatedAt());
        accessCardCommand.setUpdatedAt(source.getUpdatedAt());

        return accessCardCommand;
    }
}
