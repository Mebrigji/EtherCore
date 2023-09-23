package nets.tools.objects;

import nets.tools.model.TimedProgress;

public class TimedProgressController implements TimedProgress {

    private long value, requiredValue, total;
    private Runnable runnable = () -> {};

    @Deprecated
    public long value() {
        return value;
    }

    @Deprecated
    public void value(long value) {
        this.value = value;
    }

    @Override
    public void total(long total) {
        this.total = total;
    }

    @Override
    public long total() {
        return total;
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
        long remain = requiredValue - System.currentTimeMillis();
        return 100.0 - (remain * 100.0 / total);
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
