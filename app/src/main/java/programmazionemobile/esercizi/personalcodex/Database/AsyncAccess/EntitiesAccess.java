package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;

public class EntitiesAccess {
    private final EntitiesDAO dao;

    public EntitiesAccess(EntitiesDAO dao) {
        this.dao = dao;
    }

    public ArrayList<FD03_ENTITIES> getAll(long idSection){
        FutureTask<List<FD03_ENTITIES>> task = new FutureTask<>(() -> dao.get(idSection,""));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<FD03_ENTITIES> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public void update(FD03_ENTITIES entity){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(entity);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public Long insert(FD03_ENTITIES entity) {
        FutureTask<Long> task = new FutureTask<>(() -> dao.insert(entity));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);

        long ret;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
