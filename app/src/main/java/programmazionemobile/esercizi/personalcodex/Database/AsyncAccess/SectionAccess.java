package programmazionemobile.esercizi.personalcodex.Database.AsyncAccess;

import java.util.ArrayList;
import java.util.List;
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

    public ArrayList<TP02_SECTIONS> getAll(long idTemplate){
        FutureTask<List<TP02_SECTIONS>> task = new FutureTask<>(() -> dao.getAll(idTemplate));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<TP02_SECTIONS> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public void delete(long idSection){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.delete(idSection);
            return null;
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public long insert(TP02_SECTIONS section){
        FutureTask<Long> task = new FutureTask<>(() -> dao.insert(section));
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

    public ArrayList<TP02_SECTIONS> insertAndGetAll(TP02_SECTIONS section){
        FutureTask<List<TP02_SECTIONS>> task = new FutureTask<>(() -> {
            dao.insert(section);
            return dao.getAll(section.TP02_TEMPLATE_TP01);
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        ArrayList<TP02_SECTIONS> ret;
        try {
            ret = new ArrayList<>(task.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public void update(TP02_SECTIONS section){
        FutureTask<?> task = new FutureTask<>(() -> {
            dao.update(section);
            return null;
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
