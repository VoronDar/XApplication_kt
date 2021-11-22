package com.astery.xapplication.model.entities.values

/** consts for advises
 *
 * NO, I can't use enum because of firebase :(
 */
object AdviceType {
    /** often mistake or call a doctor */
    const val ALERT = 0
    /** any advice that tell you what you should do */
    const val ALWAYS_DO_IT = 2
    /** pretty unusual idea that might not work*/
    const val NEW_IDEA = 4
}