package br.com.lvb.studies.book_service.proxy;

import br.com.lvb.studies.book_service.dto.ExchangeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "exchange-service"
)
public interface ExchangeProxy {

    @GetMapping("/exchange-service/{amount}/{from}/{to}")
    ExchangeDTO getExchange(
            @PathVariable("amount") Double amount,
            @PathVariable("from") String from,
            @PathVariable("to") String to
    );
}