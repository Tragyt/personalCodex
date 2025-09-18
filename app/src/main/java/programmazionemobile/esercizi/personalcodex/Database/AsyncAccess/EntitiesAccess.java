package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;

public class EntitiesAccess {
    private final EntitiesDAO dao;

    public EntitiesAccess(EntitiesDAO dao) {
        this.dao = dao;
    }

    public ArrayList<FD03_ENTITIES> getAll(long idSection) {
        FutureTask<List<FD03_ENTITIES>> task = new FutureTask<>(() -> dao.getAll(idSection, ""));
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

    public void update(FD03_ENTITIES entity) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(entity);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public ArrayList<FD03_ENTITIES> updateAndGetAll(FD03_ENTITIES entity) {
        FutureTask<List<FD03_ENTITIES>> task = new FutureTask<List<FD03_ENTITIES>>(() -> {
            dao.update(entity);
            return dao.getAll(entity.FD03_SECTION_FD02,"");
        });

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

    public void delete(long idEntity) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(idEntity);
            return null;
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public FD03_ENTITIES get(long id){
        FutureTask<FD03_ENTITIES> task = new FutureTask<>(() -> dao.get(id));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);

        FD03_ENTITIES ret;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
