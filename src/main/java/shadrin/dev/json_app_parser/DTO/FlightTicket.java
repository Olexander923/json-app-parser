package shadrin.dev.json_app_parser.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * класс для десериализации json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class FlightTicket {
    private String origin;

    @JsonProperty("origin_name")
    private String originName;
    private String destination;

    @JsonProperty("destination_name")
    private String destinationName;

    @JsonProperty("departure_date")
    private LocalDate departureDate;

    @JsonProperty("departure_time")
    private LocalTime departureTime;

    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;

    @JsonProperty("arrival_time")
    private LocalTime arrivalTime;

    private String originTimeZone;
    private String destinationTimeZone;

    private String carrier;
    private int stops;
    private BigDecimal price;

    private List<FlightTicket> tickets;


    public List<FlightTicket> getTickets() {
        return tickets;
    }


    public String getOriginName() {
        return originName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

}
