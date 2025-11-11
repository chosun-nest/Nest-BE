-- MySQL 초기화 스크립트 (선택사항)
-- 이 파일은 MySQL 컨테이너가 처음 시작될 때 자동으로 실행됩니다.

-- 데이터베이스가 이미 존재하는지 확인
CREATE DATABASE IF NOT EXISTS nest_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 데이터베이스 사용
USE nest_db;

-- 필요한 경우 초기 테이블이나 데이터를 여기에 추가할 수 있습니다.
-- 예시:
-- CREATE TABLE IF NOT EXISTS example (
--     id BIGINT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(255) NOT NULL
-- );

-- 초기화 완료 메시지
SELECT 'Database initialization completed!' AS message;