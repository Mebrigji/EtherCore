package nets.tools.model;

public interface TimedProgress extends Progress {

    void total(long total);
    long total();

}
