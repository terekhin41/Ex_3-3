import org.junit.Test
import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun createChat() {
        val person = 1
        val member = 2

        ChatService.createChat(person, member)
        val result = ChatService.persons[person]?.get(member)

        assertEquals(Chat(person, member), result)
    }

    @Test
    fun deleteChat() {
        val person = 1
        val member = 2

        ChatService.deleteChat(person, member)
        val result = ChatService.persons[person]?.get(member)

        assertNull(result)
    }

    @Test
    fun sendMessage() {
        val person = 1
        val member = 2
        val expectedText = "TEST"

        ChatService.sendMessage(person, member, expectedText)
        val result = ChatService.persons[person]?.get(member)?.messages?.values?.iterator()?.next()?.text

        assertEquals(expectedText, result)
    }

    @Test
    fun editMessage() {
        val person = 1
        val member = 2
        val message = ChatService.sendMessage(person, member, "initial text")
        val expectedText = "TEST"

        ChatService.editMessage(message.id, expectedText)
        val result = ChatService.persons[person]?.get(member)?.messages?.get(message.id)?.text

        assertEquals(expectedText, result)
    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.persons.clear()
        ChatService.sendMessage(2, 1, "1")
        ChatService.sendMessage(3, 1, "2")

        val result = ChatService.getUnreadChatsCount(1)

        assertEquals(2, result)
    }

    @Test
    fun getUnreadChatsCount_deletedChat() {
        ChatService.persons.clear()

        val result = ChatService.getUnreadChatsCount(1)

        assertEquals(0, result)
    }
}