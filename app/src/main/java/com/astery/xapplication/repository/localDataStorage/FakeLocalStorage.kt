package com.astery.xapplication.repository.localDataStorage

import com.astery.xapplication.model.entities.*
import com.astery.xapplication.model.entities.values.AdviceType
import com.astery.xapplication.model.entities.values.EventCategory
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * test implementation of local storage
 * */
@Singleton
class FakeLocalStorage @Inject constructor() : LocalStorage {
    override suspend fun getEventsForDate(date: Calendar): List<Event> {
        val list = ArrayList<Event>()
        if (date.get(Calendar.DAY_OF_MONTH) == 2) {
            list.add(Event(1, 2, Date()))
            list.add(Event(2, 2, Date()))
            list.add(Event(3, 2, Date()))
            list.add(Event(4, 2, Date()))
        } else if (date.get(Calendar.DAY_OF_MONTH) == 1) {
            list.add(Event(1, 1, Date()))
            list.add(Event(2, 2, Date()))
        }

        return list
    }

    override suspend fun getDescriptionForEvent(event: Event): EventTemplate {
        return if (event.templateId == 1) {
            val list = getQuestionsAndSelectedAnswersForEvent(event.id!!)
            val template =
                EventTemplate(1, "name", "asdasdasdasd asd asd sad asd asd as", EventCategory.Feels)
            template.questions = list
            template
        } else {
            val template = EventTemplate(
                2,
                "You feel bad",
                "asdas adas dasdasd asdsadasdsadasdas dasd asd asdsadsad as dasd asd asd asd asd asd as as",
                EventCategory.MenstrualCycle
            )
            template.questions = getQuestionsAndSelectedAnswersForEvent(event.id!!)
            template
        }
    }

    override suspend fun getTemplatesForCategory(category: EventCategory): List<EventTemplate> {
        val list = ArrayList<EventTemplate>()
        if (category == EventCategory.Medicine) {
            list.add(
                EventTemplate(
                    1,
                    "first",
                    "ad ad asdas asd asdasd sad asdas das d",
                    EventCategory.Medicine
                )
            )
            list.add(
                EventTemplate(
                    2,
                    "second",
                    "lfklsdfljdsl fjdsdlsakfklsdjfldfjlkd ljkfa ds af dsdfadsf dsf a",
                    EventCategory.Medicine
                )
            )
        } else {
            list.add(
                EventTemplate(
                    3,
                    "third",
                    "adsdasd asd asd asd asd as dasd asd as das dsad asd asd ds asdsd fsf sd d",
                    category
                )
            )
            list.add(
                EventTemplate(
                    4,
                    "fourth",
                    "jadsdhasdjas dasd asd asd asdas asd asdasdjashdkjashdkasjhkdhasdh askd asd asd asd sd",
                    category
                )
            )
            list.add(
                EventTemplate(
                    5,
                    "fifth",
                    "lfklsdfljdsl fjdsdlsakfklsdjfldfjlkd ljkfa ds af dsdfadsf dsf a",
                    category
                )
            )
        }
        return list
    }

    override suspend fun getTemplate(id: Int): EventTemplate {
        TODO("Not yet implemented")
    }

    override suspend fun getArticle(id: Int): Article {
        return Article(
            1,
            "ASD asdasd ASDasd",
            "asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd " +
                    "asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda asdasd dasd asd asd asdas das sad asdadasdk asljdaskjd \" +\n" +
                    "                \"asjjd aslkdj lkaskd lkaskd salk lkslk dlkaljk sldlasdjl ksajlkd jlkasdl jkdasljkd jlkasdlk asljkd asda",
            112,
            21
        )
    }

    override suspend fun getEvent(id: Int): Event {
        TODO("Not yet implemented")
    }

    override suspend fun addEvent(event: Event) {
        Timber.d("ok, I've added event")
    }

    override suspend fun addTemplate(template: EventTemplate) {
        TODO("Not yet implemented")
    }

    override suspend fun addTemplates(templates: List<EventTemplate>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvents() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEventTemplates() {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }

    override suspend fun addArticle(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun getArticlesWithTag(tags: List<Int>): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getArticlesWithTagAndKeyWord(tags: List<Int>, key: String): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArticles() {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsForArticle(articleId: Int): List<Item> {
        if (articleId == 1) {
            return listOf(
                Item(
                    1,
                    "text text edssd d df da sdasd asd asd asd sadasd asd sad asdasd as",
                    "name",
                    articleId,
                    0
                ),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2),
                Item(
                    2,
                    "sad sadasd sad sad sa sdsadsa dasdsad sad sad sa sa sad asdsad sad ",
                    "name2",
                    articleId,
                    1
                ),
                Item(3, "asdsa da sad das asd sa sad sadsad sasad sad", "name3", articleId, 2)
            )
        }
        return listOf(
            Item(
                4,
                "asd sad asdas dsa das asds asd asd asdasd as",
                "name?",
                articleId,
                0
            )
        )
    }

    override suspend fun getAdvicesForItem(itemId: Int): List<Advice> {
        return if (itemId == 1) {
            listOf(
                Advice(1, 2, 3, AdviceType.DO_NOT_DO_THAT_ANYMORE, "dsdsadsad sad asd ", itemId),
                Advice(
                    1,
                    2,
                    3,
                    AdviceType.ALWAYS_DO_IT,
                    "wqe asd asd asd zxc zxc zxc asd  ",
                    itemId
                )
            )
        } else {
            listOf(Advice(1, 2, 3, AdviceType.NEW_IDEA, "wqe axc zxc asd  ", itemId))
        }
    }

    override suspend fun getQuestionsAndSelectedAnswersForEvent(eventId: Int): List<Question> {
        return when (eventId) {
            1 -> {
                listOf(
                    Question(1, "fake question1", 1, Answer(1,
                        "answer1", null, 1)),
                    Question(2, "fake question2", 1, Answer(2,
                        "answer2", null, 1)),
                    Question(3, "fake question3", 1, Answer(3,
                        "answer3", null, 1)),
                    Question(4, "fake question4", 1, Answer(4,
                        "answer4", null, 1))
                )
            }
            2 -> {
                listOf(
                    Question(1, "fake question1", 1, Answer(1,
                        "answer1", 1, 1)),
                    Question(2, "fake question2", 1, Answer(2,
                        "answer2", 2, 1)),
                    Question(3, "fake question3", 1, Answer(3,
                        "answer3", 3, 1)),
                    Question(4, "fake question4", 1, Answer(4,
                        "answer4", 4, 1))
                )
            }
            else -> {
                listOf()
            }
        }
    }

    override suspend fun addAnswer(answer: Answer) {
        TODO("Not yet implemented")
    }

    override suspend fun addQuestion(question: Question) {
        TODO("Not yet implemented")
    }

    override suspend fun addQuestions(question: List<Question>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuestions() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAnswers() {
        TODO("Not yet implemented")
    }

    override suspend fun addAnswers(answers: List<Answer>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSelectedAnswer(eventId: Int, question: Question) {
        TODO("Not yet implemented")
    }

    override suspend fun getItemBody(itemId: Int): Item {
        return Item(
            -1, "asdasdsa das asd asd kasldj askldjasjdjaskldjasjlkdjklasjdklasjlkdkasjdjlk" +
                    "askdjasljdlkasjdlkasjdaslkdklasjldjasldjklasjldjlkasjdlkaskljdjaslkdkjlasjkldjkla" +
                    "sjdklasjlkdjkasjdlkasjlk dasljk d", "sadasddas asdas d", 1, 0
        )
    }

    override suspend fun getQuestionsWithAnswers(templateId: Int): List<Question> {
        val question1 = Question(1, "sasdasdsasdsa dsad asd asdsad asd", templateId)
        val question2 = Question(
            2, "asd asd asd asd sa sa sadsa dsaasasd asd asd ",
            templateId
        )
        question1.answers = listOf(
            Answer(1, "sad asd asd asd", null, 1),
            Answer(2, "adas d sadsad asd sadsa dsad ", 1, 1)
        )
        question2.answers = listOf(
            Answer(4, "sad asd asd asd", 2, 1),
            Answer(3, "asd sadas sa das s as das dasd sa sadsa dasd", null, 2)
        )
        return listOf(question1, question2)
    }

    override suspend fun deleteEvent(event: Event) {}
}