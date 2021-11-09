package com.astery.xapplication.repository.localDataStorage.dao;
/*
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.astery.xapplication.pojo.Answer;
import com.astery.xapplication.pojo.Desire;
import com.astery.xapplication.pojo.EventTemplate;
import com.astery.xapplication.pojo.Question;
import com.astery.xapplication.pojo.only_for_db.QuestionEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface QuestionDao {


import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Query("SELECT * from questionentity WHERE parent_id = :parentId")
    @Transaction
    //@Transaction
    Single<List<Question>> getQuestionsByParents(String parentId);

    @Query("SELECT * from questionentity WHERE id = :id")
    @Transaction
    Single<Question> getQuestion(String id);


    @Query("SELECT * from answer WHERE id = :id")
    Single<Answer> getAnswer(String id);

    @Query("SELECT * from answer WHERE parent_id = :parentId")
    Single<List<Answer>> getAnswerByParent(String parentId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAnswer(Answer answer);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAnswers(List<Answer> answers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addConsequence(EventTemplate eventTemplate);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addConsequences(List<EventTemplate> eventTemplates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDesire(Desire desire);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDesires(List<Desire> desires);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addQuestion(QuestionEntity question);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addQuestions(List<QuestionEntity> questions);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAnswer(Answer answer);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAnswers(List<Answer> answers);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateConsequence(EventTemplate eventTemplate);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateConsequences(List<EventTemplate> eventTemplates);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDesire(Desire desire);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDesires(List<Desire> desires);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuestion(QuestionEntity questions);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuestions(List<QuestionEntity> questions);

}
*/
