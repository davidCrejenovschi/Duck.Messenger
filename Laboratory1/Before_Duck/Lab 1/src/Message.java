import java.time.LocalDateTime;

public class Message{

    private long id;
    private String subject;
    private String body;
    private String from;
    private String to;
    private LocalDateTime date;

    public Message(String from_init, String to_init, String subject_init){

        from = from_init;
        to = to_init;
        subject = subject_init;
        date = LocalDateTime.now();
        body = "";
        id = generateId();
    }

    public void writeAMessage(String text){

        date = LocalDateTime.now();
        body = text;
    }

    public String getBody() {
        return body;
    }

    public void modifyTheSubject(String subject_new){
        subject = subject_new;
    }

    public void modifyTheSender(String sender){
        from = sender;
    }

    public String getTheSender(){
        return from;
    }

    public void modifyTheReceiver(String receiver){
        to = receiver;
    }

    public String getTheReceiver(){
        return to;
    }

    public String getTime(){
        return date.getYear()+"-"+date.getMonth()+"-"+date.getDayOfMonth()+"  "+date.getHour()+":"+date.getMinute();
    }

    private static long generateId() {

        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.getNano() / 1_000_000;

        String rawId = "" + minute + day + millis + hour + month + second + year;

        return Long.parseLong(rawId);
    }

    @Override
    public String toString() {
        return """
           ID : %d
           FROM %s TO %s :
               subject: %s
           body: %s
           date: %s
           """.formatted(id, from, to, subject, body, date);
    }

}
