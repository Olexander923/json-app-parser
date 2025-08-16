package shadrin.dev.json_app_parser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import shadrin.dev.json_app_parser.DTO.FlightTicket;
import shadrin.dev.json_app_parser.service.FlightTicketService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class OperationListener implements CommandLineRunner {

    private final FlightTicketService ticketService;

    public OperationListener(FlightTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void run(String... args)  {
        try {
            List<FlightTicket> tickets = ticketService.parse();
            if(tickets.isEmpty()) {
                System.out.println("No tickets data available.");
                return;
            }

            Map<String,String> minFlightTimes = ticketService.calculateMinFlightTime(tickets);
            System.out.println("Minimum flight time for each carrier: ");
            minFlightTimes.forEach((carrier, duration) ->
                    System.out.println(carrier + ": " + duration)
            );

            BigDecimal priceDifference = ticketService.calculatePriceDifference(tickets);
            System.out.println("Difference between average price and the median price: " + priceDifference);

        } catch (IOException ex) {
            System.err.println("File reading error: " + ex.getMessage());
        }
    }
}
