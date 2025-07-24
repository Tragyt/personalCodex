package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;

public class CampaignsAccess {
    private final CampaignsDAO dao;

    public CampaignsAccess(CampaignsDAO dao) {
        this.dao = dao;
    }

    public ArrayList<FD01_CAMPAIGNS> getAll() {
        FutureTask<List<FD01_CAMPAIGNS>> task = new FutureTask<>(dao::getAll);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<FD01_CAMPAIGNS> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public Long insert(FD01_CAMPAIGNS campaign) {
        FutureTask<Long> task = new FutureTask<>(() -> dao.insert(campaign));

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

    public void delete(FD01_CAMPAIGNS campaign) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(campaign);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
