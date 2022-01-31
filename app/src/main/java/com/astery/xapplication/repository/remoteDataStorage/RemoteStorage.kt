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
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteStorage @Inject constructor(@ApplicationContext val context: Context) {

    val eventTemplateCollection = "EVENT_TEMPLATES"
    val questionCollection = "QUESTIONS"
    val adviceCollection = "TIPS"
    val itemCollection = "ITEMS"
    val articleCollection = "ARTICLES"


    init {
        FirebaseApp.initializeApp(context)
    }


    suspend fun getTemplatesForCategory(
        category: EventCategory,
        lastUpdated: Int
    ): List<EventTemplateFromRemote> {
        Timber.d("ask for templates with latUpdated > $lastUpdated, category = {${category.name}, ${category.ordinal}}")
        lateinit var result: List<EventTemplateFromRemote>
        val db = Firebase.firestore
        db.collection(eventTemplateCollection)
            .whereEqualTo("category", category.ordinal)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnCompleteListener { task ->
                val res = ArrayList(task.result.toObjects(EventTemplateFromRemote::class.java))
                extrudeId(res as ArrayList<RemoteEntity<EventTemplate>>, task)
                result = res
                Timber.d("got templates $result")
            }
            .addOnFailureListener { e ->
                result = listOf()
                Timber.d("got no templates. Error -  $e")
            }
            .await()
        return result
    }

    /**
     * lastUpdated doesn't mean anything. It always equal -1. It is required for repository.getValues
     * */
    suspend fun getAdvicesForItem(itemId:Int, lastUpdated: Int):List<AdviceFromRemote>{
        Timber.d("ask for advices with itemId = $itemId")
        lateinit var result: List<AdviceFromRemote>
        val db = Firebase.firestore
        db.collection(adviceCollection)
            .whereEqualTo("itemId", itemId)
            .get()
            .addOnCompleteListener { task ->
                val res = ArrayList(task.result.toObjects(AdviceFromRemote::class.java))
                extrudeId(res as ArrayList<RemoteEntity<Advice>>, task)
                result = res
                Timber.d("got advices $result")
            }
            .addOnFailureListener { e ->
                result = listOf()
                Timber.d("got no advices. Error -  $e")
            }
            .await()
        return result
    }

    /**
     * lastUpdated doesn't mean anything. It always equal -1. It is required for repository.getValues
     * */
    suspend fun getItemsForArticle(articleId:Int, lastUpdated: Int):List<ItemFromRemote>{
        Timber.d("ask for items with articleId = $articleId")
        lateinit var result: List<ItemFromRemote>
        val db = Firebase.firestore
        db.collection(itemCollection)
            .whereEqualTo("articleId", articleId)
            .get()
            .addOnCompleteListener { task ->
                val res = ArrayList(task.result.toObjects(ItemFromRemote::class.java))
                extrudeId(res as ArrayList<RemoteEntity<Item>>, task)
                result = res
                Timber.d("got items $result")
            }
            .addOnFailureListener { e ->
                result = listOf()
                Timber.d("got no items. Error -  $e")
            }
            .await()
        return result
    }

    /** lastUpdated means nothing. Return list with zero or one item*/
    suspend fun getItemById(itemId:Int, lastUpdated: Int):List<ItemFromRemote>{
        Timber.d("ask for item with itemId = $itemId")
        lateinit var result: List<ItemFromRemote>
        val db = Firebase.firestore
        db.collection(itemCollection).document(itemId.toString())
            .get()
            .addOnCompleteListener { task ->
                try{
                val res = task.result.toObject(ItemFromRemote::class.java)
                if (res != null) {
                    // TODO (check nonnull)
                    res.id = task.result.id.toInt()
                }
                result = if (res == null) listOf()
                else listOf(res)
                Timber.d("got item $result")
                } catch(e:Exception){ listOf<ItemFromRemote>()}
            }
            .addOnFailureListener { e ->
                result = listOf()
                Timber.d("got no items. Error -  $e")
            }
            .await()
        return result
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
    ): List<QuestionFromRemote> {
        Timber.d("ask for questions")
        lateinit var result: List<QuestionFromRemote>
        val db = Firebase.firestore

        val answerCommands = ArrayList<AnswerCommand>()

        db.collection(questionCollection)
            .whereEqualTo("eventTemplateId", templateId)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnCompleteListener { task ->
                result = ArrayList(task.result.toObjects(QuestionFromRemote::class.java))
                for (ind in result.indices) {
                    val i = result[ind]
                    i.id = task.result.documents[ind].id.replace(" ", "").toInt()
                    answerCommands.add(AnswerCommand(i))
                }

            }
            .addOnFailureListener { e ->
                result = listOf()
                Timber.d("got no templates. Error -  $e")
            }
            .await()
        for (i in answerCommands)
            i.loadAnswer(db)
        return result
    }


    suspend fun getImg(source:StorageSource, name: String): Bitmap? {
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
        } catch(e:java.lang.Exception){
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

        lateinit var isSuccess:WAA

        db.collection(articleCollection).document(id.toString()).update(map as Map<String, Any>).addOnSuccessListener{
            isSuccess = WAA(true)
        }.addOnFailureListener {
            isSuccess = WAA(false)
        }.addOnCanceledListener {
            isSuccess = WAA(true)
        }.await()
        return isSuccess.isCompleted
    }

    suspend fun updateAdviceField(id: Int, feedbackResult: FeedbackResult): Boolean {
        val db = Firebase.firestore
        val map = HashMap<String, FieldValue>()
        map[if (feedbackResult.field == FeedbackField.Like) "likes" else "dislikes"] =
            FieldValue.increment(if (feedbackResult.action == FeedbackAction.Do) 1 else -1)

        lateinit var isSuccess:WAA

        db.collection(adviceCollection).document(id.toString()).update(map as Map<String, Any>).addOnSuccessListener{
            isSuccess = WAA(true)
        }.addOnFailureListener {
            isSuccess = WAA(false)
        }.addOnCanceledListener {
            isSuccess = WAA(true)
        }.await()
        return isSuccess.isCompleted
    }
}

// TODO(sorry, but lateinit requires notnull, and also it doesn't let me to add Result<Bitmap>)
class WA(var bitmap: Bitmap?)
class WAA(var isCompleted: Boolean)
class AnswerCommand(private val q: Question){
    private val answerCollection = "ANSWERS"
    suspend fun loadAnswer(db:FirebaseFirestore){
        db.collection(answerCollection).whereEqualTo("questionId", q.id).get()
            .addOnCompleteListener { t ->
                Timber.d("complete answers for ${q.id}")
                q.answers = ArrayList(t.result.toObjects(Answer::class.java))
                for (k in (q.answers as ArrayList<Answer>).indices) {
                    (q.answers as ArrayList<Answer>)[k].id =
                        t.result.documents[k].id.replace(" ", "").toInt()
                }
            }.await()
    }
}

enum class StorageSource{
    Items,
    Articles,
    Templates;

    fun getFolderName():String{
        return this.name.lowercase()
    }
}
