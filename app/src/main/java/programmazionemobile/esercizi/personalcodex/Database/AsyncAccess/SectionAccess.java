package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;

public class SectionAccess {
    SectionsDAO dao;

    public SectionAccess(SectionsDAO dao){
        this.dao = dao;
    }

    public ArrayList<TP02_SECTIONS> getAll(int idTemplate){
        FutureTask<List<TP02_SECTIONS>> task = new FutureTask<List<TP02_SECTIONS>>(new Callable<List<TP02_SECTIONS>>() {
            @Override
            public List<TP02_SECTIONS> call() throws Exception {
                return dao.getAll(idTemplate);
            }
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<TP02_SECTIONS> ret = null;
        try {
            ret = new ArrayList<TP02_SECTIONS>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public void delete(TP02_SECTIONS section){
        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                dao.delete(section);
                return null;
            }
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
