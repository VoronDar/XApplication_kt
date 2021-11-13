package com.astery.xapplication.model.entities.values

/** consts for advises
 *
 * NO, I can't use enum because of firebase :(
 */
object AdviceType {
    /** don't do - mistake but not fatal*/
    const val DO_NOT_DO_THAT_ANYMORE = 0
    /** rape - your partner actions that can harm you */
    const val RAPE = 1
    /** any advice that tell you what you should do */
    const val ALWAYS_DO_IT = 2
    /** call a doctor, be aware of your health, etc */
    const val BE_CAREFULL = 3
    /** pretty unusual idea */
    const val NEW_IDEA = 4
}