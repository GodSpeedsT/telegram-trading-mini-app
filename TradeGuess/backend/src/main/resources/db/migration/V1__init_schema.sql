CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     telegram_id BIGINT UNIQUE NOT NULL,
                                     username VARCHAR(255),
    first_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS chart_segments (
                                              id BIGSERIAL PRIMARY KEY,
                                              symbol VARCHAR(50) NOT NULL,
    timeframe VARCHAR(10) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    display_candles JSONB NOT NULL,
    result_candles JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Попытки угадывания
CREATE TABLE IF NOT EXISTS guess_attempts (
                                              id BIGSERIAL PRIMARY KEY,
                                              user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    chart_segment_id BIGINT REFERENCES chart_segments(id) ON DELETE CASCADE,
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('LONG', 'SHORT')),
    is_correct BOOLEAN NOT NULL,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_users_telegram_id ON users(telegram_id);
CREATE INDEX IF NOT EXISTS idx_guess_attempts_user_date ON guess_attempts(user_id, attempted_at);
CREATE INDEX IF NOT EXISTS idx_guess_attempts_attempted_at ON guess_attempts(attempted_at);