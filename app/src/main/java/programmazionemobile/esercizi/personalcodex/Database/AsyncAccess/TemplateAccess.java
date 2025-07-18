package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.List;
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
        FutureTask<List<TP01_TEMPLATES>> task = new FutureTask<List<TP01_TEMPLATES>>(() -> dao.getAll());

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

    public long insert(TP01_TEMPLATES template){
        FutureTask<Long> task = new FutureTask<Long>(() -> {
            return dao.insert(template);
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);

        long ret = 0;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public void delete(TP01_TEMPLATES template){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(template);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void update(TP01_TEMPLATES template){
        FutureTask<?> task = new FutureTask<>(()->{
            dao.update(template);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
