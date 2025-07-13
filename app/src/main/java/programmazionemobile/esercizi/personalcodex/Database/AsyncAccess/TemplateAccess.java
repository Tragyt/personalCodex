package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.TemplatesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;

public class TemplateAccess {
    TemplatesDAO dao;

    public TemplateAccess(TemplatesDAO dao){
        this.dao = dao;
    }

    public List<TP01_TEMPLATES> getAll(){
        FutureTask<List<TP01_TEMPLATES>> task = new FutureTask<List<TP01_TEMPLATES>>(new Callable<List<TP01_TEMPLATES>>() {
            @Override
            public List<TP01_TEMPLATES> call() throws Exception {
                return dao.getAll();
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        List<TP01_TEMPLATES> ret = null;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public void insert(TP01_TEMPLATES template){
        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                dao.insert(template);
                return null;
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void delete(TP01_TEMPLATES template){
        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                dao.delete(template);
                return null;
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
