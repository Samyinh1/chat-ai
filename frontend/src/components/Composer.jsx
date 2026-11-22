import { useRef, useState } from 'react'
import { MAX_MESSAGE_CHARS } from '../lib/constants.js'

export default function Composer({ onSend, onStop, streaming = false }) {
  const [value, setValue] = useState('')
  const ref = useRef(null)

  const resize = () => {
    const el = ref.current
    if (!el) return
    el.style.height = 'auto'
    el.style.height = `${Math.min(el.scrollHeight, 200)}px`
  }

  const tooLong = value.length > MAX_MESSAGE_CHARS

  const submit = () => {
    const text = value.trim()
    if (!text || streaming || tooLong) return
    onSend?.(text)
    setValue('')
    requestAnimationFrame(resize)
  }

  const onKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      submit()
    } else if (e.key === 'Escape' && streaming) {
      onStop?.()
    }
  }

  return (
    <div className="border-t border-gray-200 p-3 dark:border-gray-800">
      <div className="mx-auto flex max-w-3xl items-end gap-2">
        <textarea
          ref={ref}
          rows={1}
          value={value}
          onChange={(e) => {
            setValue(e.target.value)
            resize()
          }}
          onKeyDown={onKeyDown}
          disabled={streaming}
          placeholder="Send a message..."
          aria-label="Message"
          className="max-h-52 flex-1 resize-none rounded-lg border border-gray-300 bg-transparent p-2 text-sm outline-none focus:border-indigo-500 disabled:opacity-60 dark:border-gray-700"
        />
        {streaming ? (
          <button
            type="button"
            onClick={onStop}
            aria-label="Stop generating"
            className="rounded-lg bg-red-600 px-4 py-2 text-sm text-white hover:bg-red-500"
          >
            Stop
          </button>
        ) : (
          <button
            type="button"
            onClick={submit}
            disabled={!value.trim() || tooLong}
            aria-label="Send message"
            className="rounded-lg bg-indigo-600 px-4 py-2 text-sm text-white hover:bg-indigo-500 disabled:opacity-40"
          >
            Send
          </button>
        )}
      </div>
      {tooLong && (
        <p className="mx-auto mt-1 max-w-3xl text-xs text-red-500" role="alert">
          Message exceeds {MAX_MESSAGE_CHARS} characters ({value.length}).
        </p>
      )}
    </div>
  )
}
