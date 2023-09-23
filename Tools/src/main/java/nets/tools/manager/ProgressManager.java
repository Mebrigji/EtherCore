package nets.tools.manager;

import nets.tools.model.Progress;
import nets.tools.model.TimedProgress;
import nets.tools.objects.ProgressController;
import nets.tools.objects.TimedProgressController;

public class ProgressManager {

    private static ProgressManager instance;

    public static ProgressManager getInstance() {
        if(instance == null) instance = new ProgressManager();
        return instance;
    }

    public Progress create(long requiredValue){
        Progress progress = new ProgressController();
        progress.requiredValue(requiredValue);
        return progress;
    }

    public TimedProgress createTimed(long total){
        TimedProgress progress = new TimedProgressController();
        progress.total(total);
        progress.requiredValue(System.currentTimeMillis() + total);
        return progress;
    }

}
