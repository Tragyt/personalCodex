package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

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

    public ArrayList<FD02_CAMPAIGNS_SECTIONS> getAllSections(long idCampaign){
        FutureTask<List<FD02_CAMPAIGNS_SECTIONS>> task = new FutureTask<>(() -> dao.getAllSections(idCampaign));
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

    public ArrayList<FD03_ENTITIES> getAllEntities(long idCampaign){
        FutureTask<List<FD03_ENTITIES>> task = new FutureTask<>(() -> dao.getAllEntities(idCampaign));
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

    public ArrayList<FD04_BONDS> getAllBonds(long idCampaign){
        FutureTask<List<FD04_BONDS>> task = new FutureTask<>(() -> dao.getAllBonds(idCampaign));
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

    public void delete(long idCampaign) {
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(idCampaign);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public void update(FD01_CAMPAIGNS campaign){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(campaign);
            return null;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public FD01_CAMPAIGNS get(long idCampaign){
        FutureTask<FD01_CAMPAIGNS> task = new FutureTask<>(()-> dao.get(idCampaign));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);

        FD01_CAMPAIGNS ret;
        try {
            ret = task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
