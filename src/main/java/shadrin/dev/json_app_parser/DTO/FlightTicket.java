package shadrin.dev.json_app_parser.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonProperty("destination_name")
    private String destinationName;

    @JsonFormat(pattern = "dd.MM.yy")
    @JsonProperty("departure_date")
    private LocalDate departureDate;

    @JsonFormat(pattern = "H:mm")
    @JsonProperty("departure_time")
    private LocalTime departureTime;

    @JsonFormat(pattern = "dd.MM.yy")
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;

    @JsonFormat(pattern = "H:mm")
    @JsonProperty("arrival_time")
    private LocalTime arrivalTime;

    private String carrier;
    private int stops;
    private BigDecimal price;

    private List<FlightTicket> tickets;

    public List<FlightTicket> getTickets() {return tickets;}

    public String getOrigin() {return origin;}

    public BigDecimal getPrice() {return price;}

    public int getStops() {return stops;}

    public String getCarrier() {return carrier;}

    public String getOriginName() {return originName;}

    public String getDestinationName() {return destinationName;}

    public LocalDate getDepartureDate() {return departureDate;}

    public LocalTime getDepartureTime() {return departureTime;}

    public LocalDate getArrivalDate() {return arrivalDate;}

    public LocalTime getArrivalTime() {return arrivalTime;}

}
