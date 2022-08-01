import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    val person : Int,
    val member : Int
    ) {
    var messages = LinkedHashMap<Int, Message>()
    var numberOfUnReadMessages : Int = 0
    var lastMessageDate : Long = 0

    fun addMessage(m : Message) {
        numberOfUnReadMessages += 1
        lastMessageDate = m.date
        messages[m.id] = m
    }

    fun deleteMessage(id : Int) : Boolean {
        return if (messages.remove(id) != null) {
            if (messages.isEmpty()) ChatService.deleteChat(person, member)
            true
        } else false
    }

    override fun toString(): String {
        return "Чат с id = $member (Непрочитанных сообщений $numberOfUnReadMessages)"
    }

    data class Message(
        val id : Int,
        val person : Int,
        val member: Int,
        var text : String,
        val date: Long = Date().time,
        var isRead : Boolean = false,
        var isEdited : Boolean = false
    ) {
        override fun toString(): String {
            val stringDate = SimpleDateFormat("hh:mm:ss").format(date)
            return "id = $person ($stringDate): $text. Прочитано=$isRead, Изменено=$isEdited"
        }
    }
}