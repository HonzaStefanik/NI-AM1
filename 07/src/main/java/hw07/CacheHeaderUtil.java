package hw07;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CacheHeaderUtil {

    private CacheHeaderUtil() {

    }

    public static String calculateWeakEtagHeader(List<Tour> tours) {
        StringBuilder weakEtag = new StringBuilder("W/");
        if (tours.isEmpty()) {
            return weakEtag.toString();
        }
        weakEtag.append("\"");
        for (Tour tour : tours) {
            weakEtag.append(tour.getId()).append(tour.getDescription());
        }
        weakEtag.append("\"");
        return weakEtag.toString();
    }

    public static String calculateStrongEtagHeader(List<Tour> tours) {
        return String.valueOf(tours.hashCode());
    }


    public static boolean isCacheUpToDate(String requestEtag, String ifModifiedHeader, ZonedDateTime lastModifiedDate, List<Tour> tours) {
        if (requestEtag == null && ifModifiedHeader == null) {
            return false;
        }
        if (requestEtag != null && ifModifiedHeader == null) {
            return doesEtagMatch(requestEtag, tours);
        }
        if (requestEtag == null && ifModifiedHeader != null) {
            return isIfModifiedSmallerThanActual(ifModifiedHeader, lastModifiedDate);
        }
        return doesEtagMatch(requestEtag, tours) || isIfModifiedSmallerThanActual(ifModifiedHeader, lastModifiedDate);
    }

    private static boolean doesEtagMatch(String etag, List<Tour> tours) {
        String verifyEtag;
        if (etag.startsWith("W/")) {
            verifyEtag = calculateWeakEtagHeader(tours);
        } else {
            verifyEtag = calculateStrongEtagHeader(tours);
        }
        return verifyEtag.equals(etag);
    }

    private static boolean isIfModifiedSmallerThanActual(String ifModifiedHeader, ZonedDateTime lastModifiedDate) {
        ZonedDateTime ifModifiedDate = ZonedDateTime.parse(ifModifiedHeader, DateTimeFormatter.RFC_1123_DATE_TIME);
        return ifModifiedDate.isAfter(lastModifiedDate) || ifModifiedDate.isEqual(lastModifiedDate);
    }

}
