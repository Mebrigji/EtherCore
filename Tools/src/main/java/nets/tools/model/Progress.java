package nets.tools.model;

public interface Progress {

    long value();
    void value(long value);

    long requiredValue();
    void requiredValue(long requiredValue);

    double getProgress();

    void afterMeetingExpectations(Runnable runnable);
    Runnable afterMeetingExpectations();

}
