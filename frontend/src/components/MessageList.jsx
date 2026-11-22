import { useEffect, useRef } from 'react'
import MessageBubble from './MessageBubble.jsx'

export default function MessageList({ messages }) {
  const endRef = useRef(null)

  useEffect(() => {
    endRef.current?.scrollIntoView({ block: 'end' })
  }, [messages])

  return (
    <div role="log" aria-live="polite" className="flex-1 overflow-y-auto p-4">
      <div className="mx-auto flex max-w-3xl flex-col gap-4">
        {messages.map((m) => (
          <MessageBubble key={m.id} message={m} />
        ))}
        <div ref={endRef} />
      </div>
    </div>
  )
}
