import EmptyState from '../components/EmptyState.jsx'
import Composer from '../components/Composer.jsx'

export default function NewChatPage() {
  // TODO: on send -> create session, navigate to /chat/:id, start stream
  const handleSend = (text) => console.log('new chat:', text)

  return (
    <>
      <EmptyState onPick={handleSend} />
      <Composer onSend={handleSend} />
    </>
  )
}
