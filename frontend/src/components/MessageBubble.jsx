import ReactMarkdown from 'react-markdown'

export default function MessageBubble({ message }) {
  const isUser = message.role === 'user'
  return (
    <div className={`flex ${isUser ? 'justify-end' : 'justify-start'}`}>
      <div
        className={`max-w-[85%] rounded-2xl px-4 py-2 text-sm ${
          isUser
            ? 'bg-indigo-600 text-white'
            : 'bg-gray-100 text-gray-900 dark:bg-gray-800 dark:text-gray-100'
        }`}
      >
        {isUser ? (
          <p className="whitespace-pre-wrap">{message.content}</p>
        ) : (
          <div className="space-y-2 [&_code]:rounded [&_code]:bg-black/10 [&_code]:px-1">
            <ReactMarkdown>{message.content}</ReactMarkdown>
          </div>
        )}
        {message.status === 'STOPPED' && <p className="mt-1 text-xs italic opacity-70">Stopped</p>}
        {message.status === 'ERROR' && <p className="mt-1 text-xs text-red-500">Something went wrong</p>}
      </div>
    </div>
  )
}
