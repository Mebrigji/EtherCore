package nets.tools.factory;

import net.saidora.api.PluginTask;
import nets.tools.manager.QueueManager;

public class TaskFactory {

    public PluginTask handleQueueTask(){
        return new PluginTask(500L, () -> {
            try {
                QueueManager.getInstance().save(10);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}
