package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.BondsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

public class BondsAccess {
    private final BondsDAO dao;

    public BondsAccess(BondsDAO dao) {
        this.dao = dao;
    }

    public ArrayList<FD04_BONDS> getAll(long idEntity) {
        FutureTask<List<FD04_BONDS>> task = new FutureTask<>(() -> dao.getAll(idEntity));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<FD04_BONDS> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public void insert(FD04_BONDS bond) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.insert(bond);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void update(FD04_BONDS bond) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(bond);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void delete(long idEntity1, long idEntity2) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(idEntity1, idEntity2);
            return null;
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
