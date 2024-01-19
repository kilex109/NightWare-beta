package nightware.main;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PodpiskoyUTILITA {

    public static String setTill(String till) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime expiryTime = Instant.ofEpochSecond(Long.parseLong(till)).atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59);

        long remainingTime = expiryTime.atZone(ZoneId.systemDefault()).toEpochSecond() - currentTime.atZone(ZoneId.systemDefault()).toEpochSecond();

        if (remainingTime <= 0) {
            System.out.println("Подписка закончилась");
            System.exit(0);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String expiryTimeString = expiryTime.format(formatter);

        System.out.println("Дата и время окончания подписки: " + expiryTimeString);
        return expiryTimeString;
    }
}

