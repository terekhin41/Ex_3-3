import java.util.*
import kotlin.collections.LinkedHashMap

object ChatService {
    private var messageId : Int = 0
    private var messagesStorage = hashMapOf<Int, Chat.Message>()
    var persons = LinkedHashMap<Int, LinkedHashMap<Int, Chat>>()

    fun createChat(person : Int, member : Int) : Chat {
        return persons.getOrPut(person) { LinkedHashMap() }
            .getOrPut(member) { Chat(person, member) }
            .also { it.open() }
    }

    fun deleteChat(person: Int, member: Int) {
        persons[person]?.remove(member)
        getSortedChats(person)
    }

     private fun getSortedChats(person: Int) : List<Chat> {
        println("\nЧаты пользователя id = $person:")
        val sortedChatList = persons[person]?.values?.sortedWith(ChatComparator) ?: emptyList()
        if (sortedChatList.isEmpty()) println("Создайте чат, чтобы начать общение")
        else sortedChatList.forEach { println(it) }
        return sortedChatList
    }

    fun sendMessage(
        person: Int,
        member: Int,
        text : String,
    ) : Chat.Message {
        messageId += 1
        val newMessage = Chat.Message(messageId, person, member, text)
        messagesStorage[messageId] = newMessage
        persons.getOrPut(person) { LinkedHashMap() }
            .getOrPut(member) { Chat(person,member) }
            .addMessage(newMessage)
        persons.getOrPut(member) { LinkedHashMap() }
            .getOrPut(person) { Chat(member, person) }
            .addMessage(newMessage)
        return newMessage
    }

    fun editMessage(id : Int, newText : String) : Boolean {
        if (newText.isEmpty() || messagesStorage[id] == null) return false
        val message = messagesStorage[id]!!
        return if (message.text == newText) true
        else {
            message.text = newText
            message.isEdited = true
            true
        }
    }

    fun getUnreadChatsCount(person: Int) : Int {
        return persons[person]?.values
            ?.fold(0) {
                    sum, element -> sum + if (element.numberOfUnReadMessages > 0) 1 else 0
            } ?: 0
    }

     private fun Chat.open() {
        this.numberOfUnReadMessages = 0
        val lastMessageId = if (this.messages.isNotEmpty()) {
            this.messages.iterator().next().value.id
        } else 0
        println("\nЧат id = $person " +
                "с id = $member " +
                (if (lastMessageId != 0) "id последнего сообщения = $lastMessageId " else "") +
                "(кол-во сообщений: ${this.messages.size}):"
        )
        messages.values
            .also { if (it.isEmpty()) println("Нет сообщений") }
            .forEach {
                it.isRead = true
                println(it)
            }
    }

    object ChatComparator : Comparator<Chat> {
        override fun compare(first: Chat, second: Chat): Int {
            val firstCompare = second.lastMessageDate.compareTo(first.lastMessageDate)
            return if (firstCompare == 0) {
                second.member.compareTo(first.member)
            } else firstCompare
        }
    }
}


