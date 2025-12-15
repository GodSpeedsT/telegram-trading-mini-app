CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       telegram_id BIGINT UNIQUE NOT NULL,
                       username VARCHAR(255),
                       first_name VARCHAR(255),
                       roles VARCHAR(50),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chart_segments (
                                id BIGSERIAL PRIMARY KEY,
                                display_candles JSONB NOT NULL,
                                result_candles JSONB NOT NULL,
                                price_at_decision DECIMAL(10, 2),
                                price_at_target DECIMAL(10, 2),
                                calculated_direction INTEGER CHECK (calculated_direction IN (1, -1))
);

CREATE TABLE IF NOT EXISTS guess_attempts (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                                chart_segment_id BIGINT REFERENCES chart_segments(id) ON DELETE CASCADE,
                                user_direction VARCHAR(10) CHECK (user_direction IN ('LONG', 'SHORT')),
                                is_correct BOOLEAN NOT NULL,
                                attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO chart_segments (display_candles, result_candles, price_at_decision, price_at_target, calculated_direction) VALUES
(
    '[{"t": 1672531200000, "o": 100, "h": 105, "l": 99, "c": 104, "v": 1000},
      {"t": 1672534800000, "o": 104, "h": 108, "l": 103, "c": 107, "v": 1200},
      {"t": 1672538400000, "o": 107, "h": 110, "l": 106, "c": 109, "v": 1100}]',
    '[{"t": 1672542000000, "o": 109, "h": 115, "l": 108, "c": 114, "v": 1300},
      {"t": 1672545600000, "o": 114, "h": 118, "l": 113, "c": 117, "v": 1400}]',
    109.00,
    117.00,
    1
),
(
    '[{"t": 1672556400000, "o": 200, "h": 202, "l": 195, "c": 196, "v": 2000},
      {"t": 1672560000000, "o": 196, "h": 198, "l": 190, "c": 192, "v": 1800},
      {"t": 1672563600000, "o": 192, "h": 193, "l": 185, "c": 186, "v": 1900}]',
    '[{"t": 1672567200000, "o": 186, "h": 188, "l": 180, "c": 182, "v": 2100},
      {"t": 1672570800000, "o": 182, "h": 184, "l": 175, "c": 177, "v": 2200}]',
    186.00,
    177.00,
    -1
);

INSERT INTO users (telegram_id,username,first_name,roles,created_at) VALUES
(
        934084397,
        'GodSpeed_Ok',
        'Admin',
        'ROLE_SUPER_ADMIN',
        NOW()
) ON CONFLICT (telegram_id) DO UPDATE SET roles = 'ROLE_SUPER_ADMIN';

CREATE INDEX idx_attempts_user ON guess_attempts(user_id);
CREATE INDEX idx_attempts_time ON guess_attempts(attempted_at);