package br.com.lvb.studies.greeting_service.controller;

import br.com.lvb.studies.greeting_service.config.GreetingConfiguration;
import br.com.lvb.studies.greeting_service.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    @Autowired
    private GreetingConfiguration greetingConfiguration;

    private static final String template = "%s, %s!";
    private final AtomicLong counter = new AtomicLong();

    // http://localhost:8080/greeting?name=Lucas
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "") String name) {

        if (name.isEmpty()) name = greetingConfiguration.defaultValue();

        return  new Greeting(counter.incrementAndGet(), String.format(template, greetingConfiguration.greeting() ,name));
    }
}
