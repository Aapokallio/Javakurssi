package tamk.ohsyte;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import tamk.ohsyte.commands.ListProviders;
import tamk.ohsyte.commands.ListEvents;
import tamk.ohsyte.datamodel.*;
import tamk.ohsyte.providers.CSVEventProvider;
import tamk.ohsyte.providers.web.EventDeserializer;
import tamk.ohsyte.providers.web.WebEventProvider;

@Command(name = "today", subcommands = {ListProviders.class, ListEvents.class}, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();
        var serverAddress = "https://todayserver-89bb2a1b2e80.herokuapp.com/";
        var serverEventsPath = "api/v1/events";
        String eventProviderId = "web";

        try {
            URI serverUri = new URI(serverAddress + serverEventsPath);
            manager.addEventProvider(
                    new WebEventProvider(serverUri, eventProviderId)
            );
        } catch (URISyntaxException e) {
            System.err.println("Failed to create server URI: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Today()).execute(args);
        System.exit(exitCode);
    }
}
