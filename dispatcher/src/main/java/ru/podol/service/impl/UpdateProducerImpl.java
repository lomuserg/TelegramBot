package ru.podol.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.podol.service.UpdateProducer;

@Component
@Log4j
public class UpdateProducerImpl implements UpdateProducer {
    @Override
    public void produce(String rabbitQueue, Update update){
        log.debug(update.getMessage().getText());
    }
}
