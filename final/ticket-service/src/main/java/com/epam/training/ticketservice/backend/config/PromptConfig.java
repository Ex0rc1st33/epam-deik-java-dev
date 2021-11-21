package com.epam.training.ticketservice.backend.config;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import static org.jline.utils.AttributedStyle.BLUE;
import static org.jline.utils.AttributedStyle.DEFAULT;

@Component
public class PromptConfig implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Ticket service>", DEFAULT.foreground(BLUE));
    }
}
