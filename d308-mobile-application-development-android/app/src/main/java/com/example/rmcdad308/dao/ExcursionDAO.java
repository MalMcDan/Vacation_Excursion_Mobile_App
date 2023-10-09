package com.example.rmcdad308.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rmcdad308.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("Select * from EXCURSIONS order by excursionId ASC")
    List<Excursion> getAllExcursions();

    @Query("Select * from Excursions Where vacationId= :vacationId order by excursionId ASC")
    List<Excursion> getAllAssociatedExcursions(int vacationId);
}
