import { Link, useParams } from 'react-router-dom'
import MessageList from '../components/MessageList.jsx'
import Composer from '../components/Composer.jsx'
import { mockMessages } from '../lib/mockData.js'

export default function ChatPage() {
  const { sessionId } = useParams()
  const messages = mockMessages[sessionId] // TODO: replace with useMessages(sessionId)

  if (!messages) {
    return (
      <div className="flex flex-1 flex-col items-center justify-center gap-3">
        <p>Chat not found.</p>
        <Link to="/" className="text-indigo-600 underline">
          Start a new chat
        </Link>
      </div>
    )
  }

  return (
    <>
      <MessageList messages={messages} />
      <Composer onSend={(text) => console.log('send:', text)} />
    </>
  )
}
