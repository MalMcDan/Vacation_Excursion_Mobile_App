package com.example.rmcdad308.Database;

import android.app.Application;

import com.example.rmcdad308.dao.ExcursionDAO;
import com.example.rmcdad308.dao.VacationDAO;
import com.example.rmcdad308.entities.Excursion;
import com.example.rmcdad308.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO;
    private Vacation vacationById;
    private List<Vacation> mALLVacations;
    private List<Excursion> mAllExcursions;


    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db=VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO=db.vacationDAO();
        mExcursionDAO=db.excursionDAO();
    }
    public List<Vacation>getAllVacations(){
        databaseExecutor.execute(()->{
            mALLVacations=mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mALLVacations;
    }
    public Vacation getVacationById(int id) {
        databaseExecutor.execute(() -> {
            vacationById = mVacationDAO.getVacationByID(id);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vacationById;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(()->{
            mVacationDAO.insert(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.update(vacation);
        });
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Excursion>getAllExcursions(){
        databaseExecutor.execute(()->{
            mAllExcursions=mExcursionDAO.getAllExcursions();
        });
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }
    public void insert(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
