-- KEYS[1] 是请求标识（如：user_id），
-- ARGV[1] 是限流的最大请求数 (如：100)，
-- ARGV[2] 是窗口的大小（以毫秒为单位，如：1000毫秒）,
-- ARGV[3] 是当前时间戳（以毫秒为单位）
local user_key = KEYS[1]
local threshold = tonumber(ARGV[1])  -- 最大请求数
local window_size = tonumber(ARGV[2])  -- 窗口大小（毫秒）
local current_time = tonumber(ARGV[3])  -- 当前时间戳（毫秒）

-- 删除过期的请求
local window_start_time = current_time - window_size  -- 计算窗口开始时间

-- 删除当前窗口前的请求（过期请求）
redis.call("ZREMRANGEBYSCORE", user_key, 0, window_start_time)

-- 统计当前窗口内的请求数
local request_count = redis.call("ZCOUNT", user_key, window_start_time, current_time)

-- 如果请求数超过限制，拒绝请求
if request_count >= threshold then
    return "fail"  -- 超过限制，拒绝请求
end

-- 否则，允许请求，插入当前请求时间戳
redis.call("ZADD", user_key, current_time, current_time)

-- 返回 0，表示请求被接受
return "success"
