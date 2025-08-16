package shadrin.dev.json_app_parser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shadrin.dev.json_app_parser.parser.JsonParser;
import shadrin.dev.json_app_parser.DTO.FlightTicket;
import shadrin.dev.json_app_parser.property.FileProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * сервис отвечающий за операции с билетами
 */
@Service
public class FlightTicketService implements JsonParser {
    private final FileProperties fileProperties;

    @Autowired
    public FlightTicketService(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Override
    public List<FlightTicket> parse() throws IOException {
        try {
            String filePath = fileProperties.getFilePath();
            // Поддержка classpath путей
            if (filePath.startsWith("classpath:")) {
                filePath = filePath.substring(10); // Убираем "classpath:"
                InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
                if (is == null) {
                    throw new FileNotFoundException("File not found in classpath: " + filePath);
                }
                String content = new String(is.readAllBytes());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                var ticket = objectMapper.readValue(content, FlightTicket.class);
                return ticket.getTickets();
            } else {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                var ticket = objectMapper.readValue(content, FlightTicket.class);
                return ticket.getTickets();
            }
        } catch (IOException ex) {
            System.err.println("File reading error: " + ex.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * рассчет минимального времени полета между городами
     */
    public Map<String, String> calculateMinFlightTime(List<FlightTicket> tickets) {
        return tickets.stream()
                .filter(ticket -> "Vladivostok".equalsIgnoreCase(ticket.getOriginName()) &&
                        "Tel Aviv".equalsIgnoreCase(ticket.getDestinationName()))

                .collect(Collectors.groupingBy(FlightTicket::getCarrier,
                        Collectors.minBy(Comparator.comparing(this::calculateFlightDuration))))
                .entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateFlightDuration(entry.getValue().get())
                ));

    }

    /**
     * рассчет времени полета
     * добавил фиксированные часовые пояса для избежания ошибок
     */
    private String calculateFlightDuration(FlightTicket ticket) {
        ZoneId vladivostokZone = ZoneId.of("Asia/Vladivostok");
        ZoneId telAvivZone = ZoneId.of("Asia/Tel_Aviv");

        ZonedDateTime departure = ZonedDateTime.of(
                ticket.getDepartureDate(),
                ticket.getDepartureTime(),
                vladivostokZone
        );

        ZonedDateTime arrival = ZonedDateTime.of(
                ticket.getArrivalDate(),
                ticket.getArrivalTime(),
                telAvivZone
        );

        Duration duration = Duration.between(departure, arrival);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%d h %d min", hours, minutes);


    }

    public BigDecimal calculatePriceDifference(List<FlightTicket> tickets) {
        List<BigDecimal> prices = tickets.stream()
                .filter(ticket -> "Vladivostok".equalsIgnoreCase(ticket.getOriginName()) &&
                        "Tel Aviv".equalsIgnoreCase(ticket.getDestinationName()))
                .map(FlightTicket::getPrice)
                .sorted()
                .collect(Collectors.toList());

        if (prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal averagePrice = prices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);

        BigDecimal medianPrice;
        if (prices.size() % 2 == 0) {
            BigDecimal lower = prices.get(prices.size() / 2 - 1);
            BigDecimal higher = prices.get(prices.size() / 2);
            medianPrice = lower.add(higher).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else {
            medianPrice = prices.get(prices.size() / 2);
        }

        return averagePrice.subtract(medianPrice);

    }

}
