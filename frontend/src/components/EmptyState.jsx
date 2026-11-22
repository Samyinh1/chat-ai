const SUGGESTIONS = [
  'Explain how Server-Sent Events work',
  'Write a Spring Boot REST controller',
  'Summarize the pros and cons of microservices',
  'Help me debug a React re-render issue',
]

export default function EmptyState({ onPick }) {
  return (
    <div className="flex flex-1 flex-col items-center justify-center gap-6 p-6 text-center">
      <h1 className="text-2xl font-semibold">How can I help you today?</h1>
      <div className="grid w-full max-w-xl gap-2 sm:grid-cols-2">
        {SUGGESTIONS.map((s) => (
          <button
            key={s}
            type="button"
            onClick={() => onPick?.(s)}
            className="rounded-lg border border-gray-200 p-3 text-left text-sm hover:bg-gray-50 dark:border-gray-700 dark:hover:bg-gray-800"
          >
            {s}
          </button>
        ))}
      </div>
    </div>
  )
}
