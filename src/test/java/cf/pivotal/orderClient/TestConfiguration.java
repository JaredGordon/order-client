package cf.pivotal.orderClient;

import feign.Feign;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Configuration
@ComponentScan(basePackages={"cf.pivotal.orderClient"})
public class TestConfiguration {

    @Bean
    public HoldingRepository holdingRepository() {
        return Feign
                .builder()
                .encoder(new OrderEncoder())
                .decoder(new OrderDecoder())
                .target(HoldingRepository.class,
                        "http://order-service.cfapps.io");
//                        "http://localhost:8080");
    }

    @Bean
    public OrderRepository orderRepository() {
        return Feign
                .builder()
                .encoder(new OrderEncoder())
                .decoder(new OrderDecoder())
                .target(OrderRepository.class,
                       "http://order-service.cfapps.io");
//                        "http://localhost:8080");
    }

    @Bean(name = "rtQuoteService")
    public QuoteService quoteService() {
        QuoteService quoteService = Mockito.mock(QuoteService.class);
        when(quoteService.findBySymbol(any(String.class))).thenReturn(quote());

        return quoteService;
    }

    private Quote quote() {
        Quote quote = new Quote();
        quote.setSymbol("GOOG");
        quote.setHigh(new BigDecimal(100.00));
        quote.setOpen1(new BigDecimal(75.00));
        quote.setPrice(new BigDecimal(98.00));
        return quote;
    }
}