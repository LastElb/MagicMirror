package de.igorlueckel.magicmirror.contentprovider;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.RecurrenceDates;
import biweekly.util.ICalDate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.ning.http.client.Response;
import de.igorlueckel.magicmirror.controller.admin.content.IcalController;
import de.igorlueckel.magicmirror.converter.LocalDateConverter;
import de.igorlueckel.magicmirror.converter.LocalDateTimeConverter;
import de.igorlueckel.magicmirror.models.User;

import de.igorlueckel.magicmirror.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * Created by Igor on 22.09.2015.
 */
@Component
public class ICalProvider extends AbstractContentProvider {

    LocalDateConverter localDateConverter = new LocalDateConverter();
    LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpService httpService;
    @Autowired
    IcalController icalController;

    @Override
    public String getName() {
        return "ical";
    }

    @Override
    public String getFrontendTemplate() {
        return "ical.html";
    }

    /**
     * Returns a {@link Map} with the calendar name as key and a {@link List} of {@link de.igorlueckel.magicmirror.contentprovider.ICalProvider.ComparableEvent} as value.
     * @param user
     * @return
     */
    @Override
    public Object getData(User user) {
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1);
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> calendarEvents = new LinkedHashMap<>();
        for (CalendarConnection calendarConnection : icalController.accounts(user.getUsername())) {
            try {
                ICalendar ical = Biweekly.parse(httpService.getAsyncHttpClient().prepareGet(calendarConnection.getUrl()).execute().get().getResponseBodyAsStream()).first();
                if (ical == null)
                    continue;
                List<ComparableEvent> events = new ArrayList<>();
                for (VEvent vEvent : ical.getEvents()) {
                    LocalDateTime eventDate = localDateTimeConverter.convertToEntityAttribute(vEvent.getDateStart().getValue());
                    if (eventDate.isAfter(now) && eventDate.isBefore(endOfDay)) {
                        events.add(new ComparableEvent().setEventDateTime(eventDate).setTitle(vEvent.getSummary().getValue()));
                    }

                    for (RecurrenceDates recurrenceDates : vEvent.getRecurrenceDates()) {
                        for (ICalDate iCalDate : recurrenceDates.getDates()) {
                            eventDate = localDateTimeConverter.convertToEntityAttribute(iCalDate);
                            if (eventDate.isAfter(now) && eventDate.isBefore(endOfDay)) {
                                events.add(new ComparableEvent().setEventDateTime(eventDate).setTitle(vEvent.getSummary().getValue()));
                            }
                        }
                    }
                    if (vEvent.getRecurrenceRule() != null) {
                        List<Date> dates = Lists.newArrayList(Iterators.limit(vEvent.getRecurrenceRule().getDateIterator(vEvent.getDateStart().getValue()), 10));
                        for (Date date : dates) {
                            eventDate = localDateTimeConverter.convertToEntityAttribute(date);
                            if (eventDate.isAfter(now) && eventDate.isBefore(endOfDay)) {
                                events.add(new ComparableEvent().setEventDateTime(eventDate).setTitle(vEvent.getSummary().getValue()));
                            }
                        }
                    }
                }
                events = events.stream().sorted((o1, o2) -> o1.eventDateTime.compareTo(o2.eventDateTime)).collect(Collectors.toList());
                calendarEvents.put(calendarConnection.getName(), events);
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return calendarEvents;
    }

    public ICalendar testConnection(CalendarConnection calendarConnection) throws Exception{
        Response response = httpService.getAsyncHttpClient().prepareGet(calendarConnection.getUrl()).execute().get();
        if (response.getStatusCode() != 200) {
            throw new Exception(response.getStatusCode() + " - " + response.getStatusText());
        }
        return Biweekly.parse(response.getResponseBodyAsStream()).first();
    }

    public static class CalendarConnection {
        String url;
        String name;

        public String getUrl() {
            return url;
        }

        public CalendarConnection setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getName() {
            return name;
        }

        public CalendarConnection setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CalendarConnection that = (CalendarConnection) o;

            if (url != null ? !url.equals(that.url) : that.url != null) return false;
            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            int result = url != null ? url.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    static class ComparableEvent {
        LocalDateTime eventDateTime;
        String title;

        public LocalDateTime getEventDateTime() {
            return eventDateTime;
        }

        public ComparableEvent setEventDateTime(LocalDateTime eventDateTime) {
            this.eventDateTime = eventDateTime;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public ComparableEvent setTitle(String title) {
            this.title = title;
            return this;
        }
    }

}
