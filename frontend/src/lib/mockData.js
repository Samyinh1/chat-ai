// Temporary mock data until the backend API exists.
export const mockSessions = [
  { id: 's1', title: 'Explain SSE streaming', updatedAt: '2026-11-22T09:00:00Z' },
  { id: 's2', title: 'Spring Boot virtual threads', updatedAt: '2026-11-21T15:30:00Z' },
  { id: 's3', title: 'New chat', updatedAt: '2026-11-20T11:10:00Z' },
]

export const mockMessages = {
  s1: [
    { id: 'm1', role: 'user', content: 'How does SSE streaming work?', status: 'COMPLETE' },
    {
      id: 'm2',
      role: 'assistant',
      content:
        'Server-Sent Events keep one HTTP response open and push `data:` frames to the client as they are produced.',
      status: 'COMPLETE',
    },
  ],
  s2: [
    { id: 'm3', role: 'user', content: 'Why use virtual threads with SseEmitter?', status: 'COMPLETE' },
    { id: 'm4', role: 'assistant', content: 'Blocking code scales because each stream is cheap.', status: 'STOPPED' },
  ],
  s3: [],
}
