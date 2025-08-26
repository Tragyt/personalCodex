package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;

public class CampaignSectionsAccess {
    private final CampaignsSectionsDAO dao;

    public CampaignSectionsAccess(CampaignsSectionsDAO dao) {
        this.dao = dao;
    }

    public ArrayList<FD02_CAMPAIGNS_SECTIONS> getAll(long idCampaign) {
        FutureTask<List<FD02_CAMPAIGNS_SECTIONS>> task = new FutureTask<>(() -> dao.getAll(idCampaign));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<FD02_CAMPAIGNS_SECTIONS> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public long insert(FD02_CAMPAIGNS_SECTIONS section) {
        FutureTask<Long> task = new FutureTask<>(()-> dao.insert(section));

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

    public void update(FD02_CAMPAIGNS_SECTIONS section){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(section);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void delete(long idSection){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(idSection);
            return null;
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public FD02_CAMPAIGNS_SECTIONS get(long id){
        FutureTask<FD02_CAMPAIGNS_SECTIONS> task = new FutureTask<>(()-> dao.get(id));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);

        FD02_CAMPAIGNS_SECTIONS ret;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
