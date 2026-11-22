CREATE TABLE chat_sessions (
    id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id      uuid         NOT NULL,
    title         varchar(120) NOT NULL DEFAULT 'New chat',
    system_prompt text,
    model         varchar(60),
    created_at    timestamptz  NOT NULL DEFAULT now(),
    updated_at    timestamptz  NOT NULL DEFAULT now()
);

CREATE TABLE chat_messages (
    id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id    uuid         NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
    role          varchar(10)  NOT NULL CHECK (role IN ('user', 'assistant')),
    content       text         NOT NULL,
    status        varchar(10)  NOT NULL DEFAULT 'COMPLETE'
                  CHECK (status IN ('COMPLETE', 'STOPPED', 'ERROR')),
    input_tokens  integer,
    output_tokens integer,
    created_at    timestamptz  NOT NULL DEFAULT now()
);

CREATE INDEX idx_sessions_owner_updated ON chat_sessions (owner_id, updated_at DESC);
CREATE INDEX idx_messages_session_created ON chat_messages (session_id, created_at);
