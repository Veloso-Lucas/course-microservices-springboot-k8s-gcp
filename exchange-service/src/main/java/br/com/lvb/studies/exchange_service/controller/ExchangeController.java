package br.com.lvb.studies.exchange_service.controller;

import br.com.lvb.studies.exchange_service.environment.InstanceInformationService;
import br.com.lvb.studies.exchange_service.model.Exchange;
import br.com.lvb.studies.exchange_service.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("exchange-service")
public class ExchangeController {

    @Autowired
    private InstanceInformationService instanceInformationService;

    @Autowired
    private ExchangeRepository exchangeRepository;

    // http://localhost:8000/exchange-service/5/USD/BRL
    @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(@PathVariable BigDecimal amount, @PathVariable String from, @PathVariable String to) {
        final Exchange exchange = exchangeRepository.findByFromAndTo(from, to);

        if (exchange == null) throw new RuntimeException("Currency Unsupported!");

        final BigDecimal conversionFactor = exchange.getConversionFactor();
        final BigDecimal convertedValue = conversionFactor.multiply(amount);

        exchange.setConvertedValue(convertedValue);
        exchange.setEnvironment("PORT " + instanceInformationService.retrieveServerPort());

        return exchange;
    }
}
