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

    public ArrayList<FD02_CAMPAIGNS_SECTIONS> getAll(int idCampaign) {
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
}
