package nets.tools.manager;

import nets.tools.model.QueueModel;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private static QueueManager instance;

    public static QueueManager getInstance() {
        if(instance == null) instance = new QueueManager();
        return instance;
    }

    private final List<QueueModel> queueModelList = new ArrayList<>();

    public void add(QueueModel model){
        if(!queueModelList.contains(model)) queueModelList.add(model);
    }

    public void save(int size){
        List<QueueModel> toSave = queueModelList.subList(0, Math.min(queueModelList.size(), size));
        toSave.forEach(QueueModel::save);
        synchronized (queueModelList){
            queueModelList.removeAll(toSave);
        }
    }
}
