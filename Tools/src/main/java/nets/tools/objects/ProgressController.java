package nets.tools.objects;

import nets.tools.model.Progress;

public class ProgressController implements Progress {

    private long value, requiredValue;
    private Runnable runnable = () -> {};

    @Override
    public long value() {
        return value;
    }

    @Override
    public void value(long value) {
        this.value = value;
    }

    @Override
    public long requiredValue() {
        return requiredValue;
    }

    @Override
    public void requiredValue(long requiredValue) {
        this.requiredValue = requiredValue;
    }

    @Override
    public double getProgress() {
        return value * 100.0 / requiredValue;
    }

    @Override
    public void afterMeetingExpectations(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Runnable afterMeetingExpectations() {
        return runnable;
    }
}
