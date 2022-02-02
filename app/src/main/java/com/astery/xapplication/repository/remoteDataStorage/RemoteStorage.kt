package com.astery.xapplication.repository.remoteDataStorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.model.remote.AdviceFromRemote
import com.astery.xapplication.model.remote.EventTemplateFromRemote
import com.astery.xapplication.model.remote.ItemFromRemote
import com.astery.xapplication.model.remote.QuestionFromRemote
import com.astery.xapplication.repository.FeedbackAction
import com.astery.xapplication.repository.FeedbackField
import com.astery.xapplication.repository.FeedbackResult
import com.astery.xapplication.repository.RemoteEntity
import com.astery.xapplication.ui.loadingState.InternetConnectionException
import com.astery.xapplication.ui.loadingState.UnexpectedBugException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@Suppress("UNCHECKED_CAST")
class RemoteStorage @Inject constructor(@ApplicationContext val context: Context) {

    private val eventTemplateCollection = "EVENT_TEMPLATES"
    private val questionCollection = "QUESTIONS"
    private val adviceCollection = "TIPS"
    private val itemCollection = "ITEMS"
    private val articleCollection = "ARTICLES"


    init {
        FirebaseApp.initializeApp(context)
    }


    suspend fun getTemplatesForCategory(
        category: EventCategory,
        lastUpdated: Int
    ): Result<List<EventTemplateFromRemote>> {
        val query = Firebase.firestore
            .collection(eventTemplateCollection)
            .whereEqualTo("category", category.ordinal)
            .whereGreaterThan("lastUpdated", lastUpdated).get()

        val will =
            "templates with latUpdated > $lastUpdated, category = {${category.name}, ${category.ordinal}}"

        return getValue(query, will) { task ->
            val res = ArrayList(task.result.toObjects(EventTemplateFromRemote::class.java))
            extrudeId(res as ArrayList<RemoteEntity<EventTemplate>>, task)
            return@getValue res
        }


    }


    /**
     * lastUpdated doesn't mean anything. It always equal -1. It is required for repository.getValues
     * */
    suspend fun getAdvicesForItem(itemId: Int, lastUpdated: Int): Result<List<AdviceFromRemote>> {
        val query = Firebase.firestore
            .collection(adviceCollection)
            .whereEqualTo("itemId", itemId).get()

        val will = "advices with itemId = $itemId"

        return getValue(query, will) { task ->
            val res = ArrayList(task.result.toObjects(AdviceFromRemote::class.java))
            extrudeId(res as ArrayList<RemoteEntity<Advice>>, task)
            return@getValue res
        }
    }

    /**
     * lastUpdated doesn't mean anything. It always equal -1. It is required for repository.getValues
     * */
    suspend fun getItemsForArticle(articleId: Int, lastUpdated: Int): Result<List<ItemFromRemote>> {
        val query = Firebase.firestore
            .collection(itemCollection)
            .whereEqualTo("articleId", articleId).get()

        val will = "items with articleId = $articleId"

        return getValue(query, will) { task ->
            val res = ArrayList(task.result.toObjects(ItemFromRemote::class.java))
            extrudeId(res as ArrayList<RemoteEntity<Item>>, task)
            return@getValue res
        }
    }

    /** lastUpdated means nothing. Return list with zero or one item*/
    suspend fun getItemById(itemId: Int, lastUpdated: Int): Result<List<ItemFromRemote>> {
        Timber.d("ask for item with itemId = $itemId")
        lateinit var result: WrapperResult<ItemFromRemote>
        try {
            val db = Firebase.firestore
            db.collection(itemCollection).document(itemId.toString())
                .get()
                .addOnCompleteListener { task ->
                    try {
                        val res = task.result.toObject(ItemFromRemote::class.java)
                        if (res != null) {
                            res.id = task.result.id.toInt()
                        }
                        result =
                            if (res == null) WrapperResult(Result.failure(UnexpectedBugException()))
                            else WrapperResult(Result.success(listOf(res)))
                    } catch (e: FirebaseFirestoreException) {
                        Timber.d("got firestore exception ${e.localizedMessage}, ${e.code}")
                        result = WrapperResult(Result.failure(InternetConnectionException()))
                    }
                }
                .addOnFailureListener { e ->
                    result = WrapperResult(Result.failure(UnexpectedBugException()))
                    Timber.d("got no items. Error -  $e")
                }
                .await()
            return result.result
        } catch (e: Exception) {
            return Result.failure(UnexpectedBugException())
        }
    }


    /** universal func to get list of values
     * @param task - db query
     * @param will - description for logging
     * @param doOnComplete - func that get task and transfer it to result
     * */
    private suspend fun <R> getValue(
        task: Task<QuerySnapshot>,
        will: String,
        doOnComplete: (Task<QuerySnapshot>) -> List<R>
    ): Result<List<R>> {
        Timber.d("ask for $will")
        lateinit var result: WrapperResult<R>
        try {
            task.addOnCompleteListener { task ->
                try {
                    result = WrapperResult(Result.success(doOnComplete(task)))
                    Timber.d("for $will got ${result.result.getOrThrow().size} elements")
                } catch (e: FirebaseFirestoreException) {
                    Timber.d("for $will got exception ${e.localizedMessage}, ${e.code}")
                    result = WrapperResult(Result.failure(UnexpectedBugException()))
                }
            }.addOnCanceledListener {
                Timber.d("for $will - cancelled")
                result = gotFailure(will, null)
            }.addOnFailureListener {
                Timber.d("for $will got exception ${it.localizedMessage}")
                result = gotFailure(will, it)
            }.await()
        } catch (e: Exception) {
            Timber.d("for $will got exception ${e.localizedMessage} ${e::class.simpleName}")
            result = gotFailure(will, e)
        }

        return result.result
    }

    private fun <T> gotFailure(will: String, e: Exception?): WrapperResult<T> {
        Timber.d("tried to get $will")
        return WrapperResult(Result.failure(UnexpectedBugException()))
    }


    /** get id from task. Transform invalid id. It's not possible to attach questions and image to events with invalid id
     * */
    private fun <T> extrudeId(
        res: ArrayList<RemoteEntity<T>>,
        task: Task<QuerySnapshot>
    ) {
        for (i in res.indices) {
            try {
                res[i].id = task.result.documents[i].id.replace(" ", "").toInt()
            } catch (e: Exception) {
                res[i].id = task.result.documents[i].id.hashCode()
                Timber.e("id of an event template is invalid. Got ${task.result.documents[i].id}. Expected Int. Transformed to ${res[i].id}")
            }
        }
    }


    suspend fun getQuestionsForTemplate(
        templateId: Int,
        lastUpdated: Int
    ): Result<List<QuestionFromRemote>> {
        val query = Firebase.firestore
            .collection(questionCollection)
            .whereEqualTo("eventTemplateId", templateId)
            .whereGreaterThan("lastUpdated", lastUpdated).get()

        val will = "questions with templateId = $templateId"

        val answerCommands = ArrayList<AnswerCommand>()
        val result = getValue(query, will) { task ->
            val list = ArrayList(task.result.toObjects(QuestionFromRemote::class.java))
            for (ind in list.indices) {
                val i = list[ind]
                i.id = task.result.documents[ind].id.replace(" ", "").toInt()
                answerCommands.add(AnswerCommand(i))
            }
            return@getValue list
        }
        for (i in answerCommands)
            i.loadAnswer(Firebase.firestore)
        return result

    }


    suspend fun getImg(source: StorageSource, name: String): Bitmap? {
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val child = storageRef.child("${source.getFolderName()}/$name.jpg")

        lateinit var wa: WA
        try {
            child.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                wa = WA(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                Timber.d("got an image '${source.getFolderName()}/$name.jpg'")
            }.addOnFailureListener {
                wa = WA(null)
                Timber.d(
                    "failed to get an image '${source.getFolderName()}/$name.jpg'\n" +
                            "exception - ${it.localizedMessage}"
                )
            }
                .await()
        } catch (e: java.lang.Exception) {
            wa = WA(null)
            Timber.d(
                "failed to get an image '${source.getFolderName()}/$name.jpg'\n" +
                        "exception - ${e.localizedMessage}"
            )
        }
        return wa.bitmap

    }

    suspend fun updateArticleField(id: Int, feedbackResult: FeedbackResult): Boolean {
        val db = Firebase.firestore
        val map = HashMap<String, FieldValue>()
        map[if (feedbackResult.field == FeedbackField.Like) "likes" else "dislikes"] =
            FieldValue.increment(if (feedbackResult.action == FeedbackAction.Do) 1 else -1)

        lateinit var isSuccess: WAA

        return try {
            db.collection(articleCollection).document(id.toString()).update(map as Map<String, Any>)
                .addOnSuccessListener {
                    isSuccess = WAA(true)
                }.addOnFailureListener {
                    isSuccess = WAA(false)
                }.addOnCanceledListener {
                    isSuccess = WAA(true)
                }.await()
            isSuccess.isCompleted
        } catch(e:java.lang.Exception){
            Timber.d("failed to update article field - got ${e.localizedMessage}")
            false
        }
    }

    suspend fun updateAdviceField(id: Int, feedbackResult: FeedbackResult): Boolean {
        val db = Firebase.firestore
        val map = HashMap<String, FieldValue>()
        map[if (feedbackResult.field == FeedbackField.Like) "likes" else "dislikes"] =
            FieldValue.increment(if (feedbackResult.action == FeedbackAction.Do) 1 else -1)

        lateinit var isSuccess: WAA

        return try {
            db.collection(adviceCollection).document(id.toString()).update(map as Map<String, Any>)
                .addOnSuccessListener {
                    isSuccess = WAA(true)
                }.addOnFailureListener {
                    isSuccess = WAA(false)
                }.addOnCanceledListener {
                    isSuccess = WAA(true)
                }.await()
            isSuccess.isCompleted
        }
        catch(e:java.lang.Exception){
            Timber.d("failed to update article field - got ${e.localizedMessage}")
            false
        }
    }
}

// because lateinit can't be used with nullable
// TODO(return result from func that get image)
class WA(var bitmap: Bitmap?)

// because lateinit can't be used with bool
class WAA(var isCompleted: Boolean)

// because lateinit can't be used with result
data class WrapperResult<T>(val result: Result<List<T>>)


class AnswerCommand(private val q: Question) {
    private val answerCollection = "ANSWERS"
    suspend fun loadAnswer(db: FirebaseFirestore) {
        db.collection(answerCollection).whereEqualTo("questionId", q.id).get()
            .addOnCompleteListener { t ->
                q.answers = ArrayList(t.result.toObjects(Answer::class.java))
                for (k in (q.answers as ArrayList<Answer>).indices) {
                    (q.answers as ArrayList<Answer>)[k].id =
                        t.result.documents[k].id.replace(" ", "").toInt()
                }
            }.await()
    }
}

/** image directories */
enum class StorageSource {
    Items,
    Articles,
    Templates;

    fun getFolderName(): String {
        return this.name.lowercase()
    }
}
