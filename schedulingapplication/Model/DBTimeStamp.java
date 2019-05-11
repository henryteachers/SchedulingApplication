package schedulingapplication.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DBTimeStamp {
    
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");    
    
    public static Timestamp convertToLocal (String s) {
        LocalDateTime utcTime = LocalDateTime.parse(s, dtf);
        ZonedDateTime zdtTime = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localTime = zdtTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime ldtTime = localTime.toLocalDateTime();
        Timestamp myTimestamp = Timestamp.valueOf(ldtTime);        
        return myTimestamp;
    }    
    
    public static Timestamp convertToUTC (LocalDateTime s) {
        LocalDateTime localTime = s;
        ZonedDateTime zdtTime = localTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcTime = zdtTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtTime = utcTime.toLocalDateTime();
        Timestamp myTimestamp = Timestamp.valueOf(ldtTime);
        return myTimestamp;
    }
}
