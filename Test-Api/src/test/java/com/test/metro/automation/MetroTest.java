package com.test.metro.automation;

import com.test.metro.api.MetroApi;
import com.test.metro.model.Directions;
import com.test.metro.model.Routes;
import com.test.metro.model.TimePointDepartures;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MetroTest {
    
    private static final Logger log = LoggerFactory.getLogger(MetroTest.class);
    
    MetroApi api = new MetroApi();
    private String METRO_URL = "https://svc.metrotransit.org/NexTrip";
    
    
    
    @Test
    public void getTimeForMyBus() {
        // Valid test with all correct values
        String time = getMyNextBusTime("921 - A Line Roseville-St Paul-Minneapolis", "Snelling & Larpenteur Station", "SOUTHBOUND");
        Assert.assertFalse(time.isEmpty());
        
        // Light Rail route will return Empty
        time = getMyNextBusTime("906 - Light Rail Shuttle - Lindbergh - Humphrey", "University Ave  and 15th Ave", "SOUTHBOUND");
        Assert.assertThat(time, Is.is(""));
    
        // Invalid values will return Empty
        time = getMyNextBusTime("", "", "");
        Assert.assertThat(time, Is.is(""));
    
    }
    
    
    /**
     * Calls the Metro Api using the given parameters
     * Returns empty if the given params does not match with the Api response
     * API response is setup to return in json format
     * @param routeName - Bus Route to be taken
     * @param busTop - Leaving from
     * @param directionValue - Going in direction
     * @return Time taking for the next bus in the given location
     */
    private String getMyNextBusTime(String routeName, String busTop, String directionValue) {
        String routeId;
        String directionId = "";
        String stopId = "";
        String time = "";
    
        Routes[] routes = api.deserializeJson(Routes[].class, METRO_URL + "/Routes?format=json");
        List<Routes> routesList = new ArrayList<>(Arrays.asList(routes));
        routeId = routesList.stream().filter(v -> v.getDescription().trim().equals(routeName.trim())).map(Routes::getRoute).collect(Collectors.joining());
        log.info("routeId:" + routeId);
    
    
        if(!routeId.isEmpty()) {
            Directions[] directions = api.deserializeJson(Directions[].class,
                String.format(METRO_URL + "/Directions/%s?format=json", routeId));
            List<Directions> dirList = new ArrayList<>(Arrays.asList(directions));
            directionId = dirList.stream().filter(v -> v.getText().equals(directionValue)).map(Directions::getValue).collect(Collectors.joining());
            log.info("directionId:" + directionId);
        }
    
        if(!directionId.isEmpty()) {
            Directions[] stops = api.deserializeJson(Directions[].class,
                String.format(METRO_URL + "/Stops/%s/%s?format=json", routeId, directionId));
            List<Directions> stopList = new ArrayList<>(Arrays.asList(stops));
            stopId = stopList.stream().filter(v -> v.getText().trim().equals(busTop.trim())).map(Directions::getValue).collect(Collectors.joining());
            log.info("stopId:" + stopId);
        }
    
        if(!stopId.isEmpty()) {
            TimePointDepartures[] timePointDepartures = api.deserializeJson(TimePointDepartures[].class,
                String.format(METRO_URL + "/%s/%s/%s?format=json", routeId, directionId, stopId));
            List<TimePointDepartures> timePointList = new ArrayList<>(Arrays.asList(timePointDepartures));
            time = calculateDiff(timePointList.get(0).getDepartureTime()) + " Minutes";
        }
        log.info("\n time : " + time);
        return time;
    }
    
    
    /**
     * Calculate the time it takes for the next bus using current time
     * @param timeStamp - departure time from metro api
     * @return - minutes
     */
    private long calculateDiff(String timeStamp) {
        timeStamp = timeStamp.substring(6,19);
        Long longTime = Long.valueOf(timeStamp).longValue();
        Date currentDate = new Date();
    
        long timeTillBus = (longTime - currentDate.getTime()) / 60000;
        log.info(timeTillBus + " Minutes");
        if(timeTillBus < 1) {
            timeTillBus = 0;
        }
        return timeTillBus;
    }
    
    
}
