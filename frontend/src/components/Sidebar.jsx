import { NavLink, useNavigate } from 'react-router-dom'
import { mockSessions } from '../lib/mockData.js'

export default function Sidebar({ onNavigate }) {
  const navigate = useNavigate()
  const sessions = mockSessions // TODO: replace with useSessions()

  return (
    <nav className="flex h-full flex-col p-3" aria-label="Chat sessions">
      <button
        type="button"
        className="mb-3 w-full rounded-lg bg-indigo-600 px-3 py-2 text-sm font-medium text-white hover:bg-indigo-500"
        onClick={() => {
          navigate('/')
          onNavigate?.()
        }}
      >
        + New chat
      </button>

      <ul className="flex-1 space-y-1 overflow-y-auto">
        {sessions.map((s) => (
          <li key={s.id}>
            <NavLink
              to={`/chat/${s.id}`}
              onClick={onNavigate}
              className={({ isActive }) =>
                `block truncate rounded-lg px-3 py-2 text-sm ${
                  isActive
                    ? 'bg-indigo-100 text-indigo-900 dark:bg-indigo-900/40 dark:text-indigo-100'
                    : 'hover:bg-gray-200 dark:hover:bg-gray-800'
                }`
              }
            >
              {s.title}
            </NavLink>
          </li>
        ))}
      </ul>
    </nav>
  )
}
