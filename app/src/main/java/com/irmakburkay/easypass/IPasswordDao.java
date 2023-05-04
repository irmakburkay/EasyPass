package com.irmakburkay.easypass;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IPasswordDao {

    @Insert
    void insertPassword(Password password);

    @Update
    void updatePassword(Password password);

    @Delete
    void deletePassword(Password password);

    @Query("SELECT * FROM password ORDER BY `order`")
    List<Password> loadAllPasswords();

    @Query("SELECT * FROM password WHERE id=:id")
    Password loadPasswordById(long id);

    @Query("DELETE FROM password WHERE id=:id")
    void deletePasswordById(long id);

    @Query("DELETE FROM password")
    void deleteAllPasswords();

}
