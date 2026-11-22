import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import Sidebar from '../components/Sidebar.jsx'

export default function AppLayout() {
  const [drawerOpen, setDrawerOpen] = useState(false)

  return (
    <div className="flex h-full bg-white text-gray-900 dark:bg-gray-900 dark:text-gray-100">
      {/* Backdrop for mobile drawer */}
      {drawerOpen && (
        <div
          className="fixed inset-0 z-20 bg-black/40 md:hidden"
          onClick={() => setDrawerOpen(false)}
          aria-hidden="true"
        />
      )}

      <aside
        className={`fixed inset-y-0 left-0 z-30 w-72 transform border-r border-gray-200 bg-gray-50 transition-transform dark:border-gray-800 dark:bg-gray-950 md:static md:translate-x-0 ${
          drawerOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <Sidebar onNavigate={() => setDrawerOpen(false)} />
      </aside>

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="flex items-center gap-2 border-b border-gray-200 p-2 dark:border-gray-800 md:hidden">
          <button
            type="button"
            aria-label="Open sidebar"
            className="rounded p-2 hover:bg-gray-100 dark:hover:bg-gray-800"
            onClick={() => setDrawerOpen(true)}
          >
            ☰
          </button>
          <span className="font-medium">Chat</span>
        </header>
        <main className="flex min-h-0 flex-1 flex-col">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
