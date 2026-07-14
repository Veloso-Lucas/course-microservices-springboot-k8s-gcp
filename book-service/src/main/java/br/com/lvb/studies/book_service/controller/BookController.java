package br.com.lvb.studies.book_service.controller;

import br.com.lvb.studies.book_service.dto.ExchangeDTO;
import br.com.lvb.studies.book_service.environment.InstanceInformationService;
import br.com.lvb.studies.book_service.model.Book;
import br.com.lvb.studies.book_service.proxy.ExchangeProxy;
import br.com.lvb.studies.book_service.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {


    @Autowired
    private InstanceInformationService instanceInformationService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ExchangeProxy exchangeProxy;

    // http://localhost:8100/book-service/1/BRL
    @Operation(summary = "Find a specific book by id")
    @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        final String port = instanceInformationService.retrieveServerPort();

        var book = bookRepository.findById(id).orElseThrow();


        final ExchangeDTO exchangeDTO = exchangeProxy.getExchange(book.getPrice(), "USD", currency);

        if (exchangeDTO != null) book.setPrice(exchangeDTO.getConvertedValue());

        book.setEnvironment(port);
        book.setCurrency(currency);
        return book;
    }

}
