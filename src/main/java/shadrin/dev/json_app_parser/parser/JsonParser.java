package shadrin.dev.json_app_parser.parser;

import shadrin.dev.json_app_parser.DTO.FlightTicket;

import java.io.IOException;
import java.util.List;

public interface JsonParser {
    List<FlightTicket> parse() throws IOException;

}
