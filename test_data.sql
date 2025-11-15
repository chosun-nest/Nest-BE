-- ============================================
-- 테스트 데이터 삽입 스크립트
-- ============================================
-- 사용 방법: MySQL에 접속하여 실행
-- docker cp [호스트_파일_경로] [컨테이너_ID 또는 이름]:[컨테이너_내_경로]
-- docker cp test_data.sql nest-mysql:/tmp
-- mysql -u root -p nest_db < test_data.sql

-- Character Set 설정
SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;
SET COLLATION_CONNECTION = utf8mb4_unicode_ci;

USE nest_db;

-- ============================================
-- MEMBER 테이블 테스트 데이터 (100명)
-- ============================================
-- 비밀번호 참고:
-- password123 → $2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K
-- test1234 → $2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX
-- user1234 → $2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX
-- admin1234 → $2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY
-- student123 → $2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ

INSERT INTO member (member_email, member_password, member_role, member_name, member_sns_url1, member_sns_url2, member_sns_url3, member_sns_url4, member_is_student, member_introduce, member_image_url, member_password_length, created_at, updated_at) VALUES
-- 관리자 계정 (5명)
('admin1@nest.com', '$2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY', 'ROLE_ADMIN', '관리자', 'https://github.com/admin', NULL, NULL, NULL, 0, '시스템 관리자입니다.', NULL, 9, NOW(), NOW()),
('admin2@nest.com', '$2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY', 'ROLE_ADMIN', '김관리', 'https://github.com/admin2', 'https://www.linkedin.com/in/admin2', NULL, NULL, 0, '백엔드 개발자 출신 관리자입니다.', NULL, 9, NOW(), NOW()),
('admin3@nest.com', '$2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY', 'ROLE_ADMIN', '이운영', 'https://github.com/admin3', NULL, NULL, NULL, 0, '프론트엔드 전문 관리자입니다.', NULL, 9, NOW(), NOW()),
('admin4@nest.com', '$2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY', 'ROLE_ADMIN', '박데브', 'https://github.com/admin4', 'https://twitter.com/admin4', NULL, NULL, 0, 'DevOps 담당 관리자입니다.', NULL, 9, NOW(), NOW()),
('admin5@nest.com', '$2a$10$2VpKJ6kyL9yLyLyLyLyLye9yZrLyMyKyZyYyYyYyYyYyYyYyYyYyY', 'ROLE_ADMIN', '최지원', 'https://github.com/admin5', NULL, NULL, NULL, 0, 'UX/UI 디자이너 출신 관리자입니다.', NULL, 9, NOW(), NOW()),

-- 학생 사용자 (70명)
('student1@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '김철수', 'https://github.com/kim-cs', 'https://www.instagram.com/kimcs/', NULL, NULL, 1, '컴퓨터공학과 3학년입니다. 백엔드 개발에 관심이 많습니다.', NULL, 10, NOW(), NOW()),
('student2@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '이영희', 'https://github.com/lee-yh', 'https://www.instagram.com/leeyh/', 'https://www.linkedin.com/in/leeyh/', NULL, 1, '소프트웨어학과 2학년입니다. React와 Vue를 공부하고 있습니다.', NULL, 10, NOW(), NOW()),
('student3@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '박민수', 'https://github.com/park-ms', NULL, NULL, NULL, 1, '전자공학과 4학년입니다. 임베디드 시스템에 흥미가 있습니다.', NULL, 10, NOW(), NOW()),
('student4@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '정수진', 'https://github.com/jung-sj', 'https://www.instagram.com/jungsj/', 'https://twitter.com/jungsj', NULL, 1, '컴퓨터공학과 3학년입니다. AI와 머신러닝을 공부 중입니다.', NULL, 10, NOW(), NOW()),
('student5@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '최지훈', 'https://github.com/choi-jh', NULL, NULL, NULL, 1, '소프트웨어학과 1학년입니다. 웹 개발을 처음 배우고 있습니다.', NULL, 10, NOW(), NOW()),
('student6@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '강민지', 'https://github.com/kang-mj', 'https://www.instagram.com/kangmj/', NULL, NULL, 1, '정보통신공학과 2학년입니다. 네트워크 보안에 관심 있습니다.', NULL, 10, NOW(), NOW()),
('student7@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '윤서현', 'https://github.com/yoon-sh', 'https://www.linkedin.com/in/yoonsh/', NULL, NULL, 1, '컴퓨터공학과 4학년입니다. 풀스택 개발자를 목표로 하고 있습니다.', NULL, 10, NOW(), NOW()),
('student8@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '임도현', 'https://github.com/lim-dh', NULL, NULL, NULL, 1, '소프트웨어학과 3학년입니다. 게임 개발에 관심이 많습니다.', NULL, 10, NOW(), NOW()),
('student9@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '한예진', 'https://github.com/han-yj', 'https://www.instagram.com/hanyj/', 'https://twitter.com/hanyj', NULL, 1, '컴퓨터공학과 2학년입니다. 데이터 분석과 시각화를 공부하고 있습니다.', NULL, 10, NOW(), NOW()),
('student10@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '신우성', 'https://github.com/shin-ws', NULL, NULL, NULL, 1, '전자공학과 3학년입니다. IoT 시스템 개발에 흥미가 있습니다.', NULL, 10, NOW(), NOW()),
('student11@gmail.com', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '오하늘', 'https://github.com/oh-hn', 'https://www.instagram.com/ohhn/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. 모바일 앱 개발자가 되고 싶습니다.', NULL, 10, NOW(), NOW()),
('student12@gmail.com', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '서준호', 'https://github.com/seo-jh', 'https://www.linkedin.com/in/seojh/', NULL, NULL, 1, '컴퓨터공학과 1학년입니다. 알고리즘 공부를 열심히 하고 있습니다.', NULL, 10, NOW(), NOW()),
('student13@naver.com', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '권민아', 'https://github.com/kwon-ma', NULL, NULL, NULL, 1, '정보통신공학과 3학년입니다. 클라우드 컴퓨팅에 관심 있습니다.', NULL, 10, NOW(), NOW()),
('student14@naver.com', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '조은지', 'https://github.com/jo-ej', 'https://www.instagram.com/joej/', 'https://twitter.com/joej', NULL, 1, '소프트웨어학과 2학년입니다. UI/UX 디자인도 함께 공부하고 있습니다.', NULL, 10, NOW(), NOW()),
('student15@daum.net', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '장태양', 'https://github.com/jang-ty', NULL, NULL, NULL, 1, '컴퓨터공학과 4학년입니다. 블록체인 기술에 관심이 많습니다.', NULL, 10, NOW(), NOW()),
('student16@hanmail.net', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '안소희', 'https://github.com/ahn-sh', 'https://www.instagram.com/ahnsh/', NULL, NULL, 1, '전자공학과 2학년입니다. 로봇공학에 흥미가 있습니다.', NULL, 10, NOW(), NOW()),
('student17@gmail.com', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '홍길동', 'https://github.com/hong-gd', 'https://www.linkedin.com/in/honggd/', NULL, NULL, 1, '소프트웨어학과 3학년입니다. 오픈소스 프로젝트에 참여하고 싶습니다.', NULL, 10, NOW(), NOW()),
('student18@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '남궁민', 'https://github.com/namgung-m', NULL, NULL, NULL, 1, '컴퓨터공학과 1학년입니다. 파이썬으로 자동화 프로그램을 만들고 있습니다.', NULL, 10, NOW(), NOW()),
('student19@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '황보람', 'https://github.com/hwang-br', 'https://www.instagram.com/hwangbr/', 'https://twitter.com/hwangbr', NULL, 1, '정보통신공학과 4학년입니다. 사이버 보안 전문가가 목표입니다.', NULL, 10, NOW(), NOW()),
('student20@chosun.ac.kr', '$2a$10$3WqLK7lzM0zMzMzMzMzMzf0zAsMyNzLzAzZzZzZzZzZzZzZzZzZzZ', 'ROLE_USER', '독고영', 'https://github.com/dokgo-y', NULL, NULL, NULL, 1, '소프트웨어학과 2학년입니다. 컴퓨터 그래픽스를 배우고 있습니다.', NULL, 10, NOW(), NOW()),
('dev1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '김개발', 'https://github.com/kim-dev', 'https://www.linkedin.com/in/kimdev/', NULL, NULL, 1, '컴퓨터공학과 3학년입니다. Spring Boot를 활용한 백엔드 개발을 공부 중입니다.', NULL, 11, NOW(), NOW()),
('dev2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '이코드', 'https://github.com/lee-code', 'https://www.instagram.com/leecode/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. Flutter로 크로스플랫폼 앱을 개발하고 있습니다.', NULL, 11, NOW(), NOW()),
('dev3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '박프론트', 'https://github.com/park-front', NULL, NULL, NULL, 1, '컴퓨터공학과 2학년입니다. TypeScript와 Next.js를 배우고 있습니다.', NULL, 11, NOW(), NOW()),
('dev4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '정백엔드', 'https://github.com/jung-back', 'https://twitter.com/jungback', NULL, NULL, 1, '정보통신공학과 3학년입니다. Node.js와 Express를 공부하고 있습니다.', NULL, 11, NOW(), NOW()),
('dev5@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '최풀스택', 'https://github.com/choi-full', 'https://www.linkedin.com/in/choifull/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. MERN 스택으로 풀스택 개발을 하고 있습니다.', NULL, 11, NOW(), NOW()),
('coder1@test.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '강코더', 'https://github.com/kang-coder', NULL, NULL, NULL, 1, '컴퓨터공학과 1학년입니다. 코딩 테스트 준비를 열심히 하고 있습니다.', NULL, 8, NOW(), NOW()),
('coder2@test.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '윤알고', 'https://github.com/yoon-algo', 'https://www.instagram.com/yoonalgo/', NULL, NULL, 1, '전자공학과 2학년입니다. 알고리즘 문제 풀이를 좋아합니다.', NULL, 8, NOW(), NOW()),
('coder3@test.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '임자바', 'https://github.com/lim-java', 'https://www.linkedin.com/in/limjava/', NULL, NULL, 1, '소프트웨어학과 3학년입니다. Java와 Kotlin을 주로 사용합니다.', NULL, 8, NOW(), NOW()),
('coder4@test.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '한파이썬', 'https://github.com/han-python', NULL, NULL, NULL, 1, '컴퓨터공학과 4학년입니다. Python으로 데이터 분석 프로젝트를 진행 중입니다.', NULL, 8, NOW(), NOW()),
('coder5@test.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '신자스', 'https://github.com/shin-js', 'https://twitter.com/shinjs', NULL, NULL, 1, '정보통신공학과 2학년입니다. JavaScript와 React를 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('hacker1@secure.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '오보안', 'https://github.com/oh-security', 'https://www.linkedin.com/in/ohsecurity/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. 웹 보안과 침투 테스트에 관심이 많습니다.', NULL, 8, NOW(), NOW()),
('hacker2@secure.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '서해커', 'https://github.com/seo-hacker', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. 화이트햇 해커가 되는 것이 목표입니다.', NULL, 8, NOW(), NOW()),
('hacker3@secure.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '권시큐', 'https://github.com/kwon-secure', 'https://www.instagram.com/kwonsecure/', NULL, NULL, 1, '정보통신공학과 2학년입니다. 암호학을 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('hacker4@secure.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '조방어', 'https://github.com/jo-defense', 'https://twitter.com/jodefense', NULL, NULL, 1, '소프트웨어학과 4학년입니다. 시스템 보안 전문가를 목표로 하고 있습니다.', NULL, 8, NOW(), NOW()),
('hacker5@secure.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '장네트워크', 'https://github.com/jang-network', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. 네트워크 보안과 방화벽 기술을 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('ai1@ml.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '안인공', 'https://github.com/ahn-ai', 'https://www.linkedin.com/in/ahnai/', NULL, NULL, 1, '컴퓨터공학과 4학년입니다. 딥러닝과 컴퓨터 비전을 연구하고 있습니다.', NULL, 11, NOW(), NOW()),
('ai2@ml.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '홍머신', 'https://github.com/hong-ml', 'https://www.instagram.com/hongml/', NULL, NULL, 1, '소프트웨어학과 3학년입니다. 머신러닝 알고리즘을 공부하고 있습니다.', NULL, 11, NOW(), NOW()),
('ai3@ml.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '남궁딥', 'https://github.com/namgung-deep', NULL, NULL, NULL, 1, '정보통신공학과 4학년입니다. 자연어 처리(NLP)에 관심이 많습니다.', NULL, 11, NOW(), NOW()),
('ai4@ml.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '황보뉴럴', 'https://github.com/hwangbo-neural', 'https://twitter.com/hwangboneural', NULL, NULL, 1, '컴퓨터공학과 2학년입니다. 강화학습으로 게임 AI를 만들고 있습니다.', NULL, 11, NOW(), NOW()),
('ai5@ml.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '독고텐서', 'https://github.com/dokgo-tensor', 'https://www.linkedin.com/in/dokgotensor/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. TensorFlow와 PyTorch를 활용한 프로젝트를 진행 중입니다.', NULL, 11, NOW(), NOW()),
('game1@dev.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '김게임', 'https://github.com/kim-game', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. Unity로 인디 게임을 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('game2@dev.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '이유니티', 'https://github.com/lee-unity', 'https://www.instagram.com/leeunity/', NULL, NULL, 1, '소프트웨어학과 2학년입니다. 3D 게임 그래픽 프로그래밍을 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('game3@dev.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '박언리얼', 'https://github.com/park-unreal', 'https://twitter.com/parkunreal', NULL, NULL, 1, '전자공학과 4학년입니다. Unreal Engine으로 VR 게임을 만들고 있습니다.', NULL, 8, NOW(), NOW()),
('game4@dev.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '정그래픽', 'https://github.com/jung-graphics', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. 게임 엔진 개발에 도전하고 있습니다.', NULL, 8, NOW(), NOW()),
('game5@dev.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '최모바일', 'https://github.com/choi-mobile', 'https://www.linkedin.com/in/choimobile/', NULL, NULL, 1, '정보통신공학과 2학년입니다. 모바일 게임 개발에 흥미가 있습니다.', NULL, 8, NOW(), NOW()),
('data1@analysis.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '강데이터', 'https://github.com/kang-data', 'https://www.linkedin.com/in/kangdata/', NULL, NULL, 1, '소프트웨어학과 4학년입니다. 빅데이터 분석과 처리를 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('data2@analysis.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '윤분석', 'https://github.com/yoon-analysis', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. 데이터 시각화 도구를 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('data3@analysis.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '임사이언스', 'https://github.com/lim-science', 'https://www.instagram.com/limscience/', NULL, NULL, 1, '정보통신공학과 2학년입니다. 데이터 과학자를 목표로 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('data4@analysis.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '한통계', 'https://github.com/han-stats', 'https://twitter.com/hanstats', NULL, NULL, 1, '소프트웨어학과 4학년입니다. 통계 분석과 예측 모델링을 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('data5@analysis.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '신빅데이터', 'https://github.com/shin-bigdata', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. Hadoop과 Spark를 활용한 빅데이터 처리를 공부 중입니다.', NULL, 8, NOW(), NOW()),
('mobile1@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '오앱', 'https://github.com/oh-app', 'https://www.linkedin.com/in/ohapp/', NULL, NULL, 1, '소프트웨어학과 2학년입니다. iOS 네이티브 앱 개발을 배우고 있습니다.', NULL, 11, NOW(), NOW()),
('mobile2@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '서안드로이드', 'https://github.com/seo-android', 'https://www.instagram.com/seoandroid/', NULL, NULL, 1, '컴퓨터공학과 3학년입니다. Kotlin으로 Android 앱을 개발하고 있습니다.', NULL, 11, NOW(), NOW()),
('mobile3@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '권플러터', 'https://github.com/kwon-flutter', NULL, NULL, NULL, 1, '정보통신공학과 4학년입니다. Flutter로 크로스플랫폼 앱을 만들고 있습니다.', NULL, 11, NOW(), NOW()),
('mobile4@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '조리액트네이티브', 'https://github.com/jo-rn', 'https://twitter.com/jorn', NULL, NULL, 1, '소프트웨어학과 2학년입니다. React Native로 모바일 앱을 개발하고 있습니다.', NULL, 11, NOW(), NOW()),
('mobile5@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '장스위프트', 'https://github.com/jang-swift', 'https://www.linkedin.com/in/jangswift/', NULL, NULL, 1, '컴퓨터공학과 4학년입니다. Swift로 iOS 앱을 개발하고 게임도 만들고 있습니다.', NULL, 11, NOW(), NOW()),
('cloud1@infra.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '안클라우드', 'https://github.com/ahn-cloud', NULL, NULL, NULL, 1, '정보통신공학과 3학년입니다. AWS와 Azure를 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('cloud2@infra.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '홍데브옵스', 'https://github.com/hong-devops', 'https://www.instagram.com/hongdevops/', NULL, NULL, 1, '컴퓨터공학과 4학년입니다. CI/CD 파이프라인 구축을 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('cloud3@infra.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '남궁쿠버', 'https://github.com/namgung-k8s', 'https://twitter.com/namgungk8s', NULL, NULL, 1, '소프트웨어학과 2학년입니다. Kubernetes를 활용한 컨테이너 오케스트레이션을 배우고 있습니다.', NULL, 8, NOW(), NOW()),
('cloud4@infra.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '황보도커', 'https://github.com/hwangbo-docker', NULL, NULL, NULL, 1, '컴퓨터공학과 3학년입니다. Docker를 이용한 마이크로서비스 아키텍처를 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('cloud5@infra.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '독고인프라', 'https://github.com/dokgo-infra', 'https://www.linkedin.com/in/dokgoinfra/', NULL, NULL, 1, '정보통신공학과 4학년입니다. 클라우드 인프라 설계와 관리를 공부하고 있습니다.', NULL, 8, NOW(), NOW()),
('iot1@embedded.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '김임베디드', 'https://github.com/kim-embedded', 'https://www.linkedin.com/in/kimembedded/', NULL, NULL, 1, '전자공학과 3학년입니다. ARM 기반 임베디드 시스템을 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('iot2@embedded.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '이아이오티', 'https://github.com/lee-iot', NULL, NULL, NULL, 1, '컴퓨터공학과 4학년입니다. IoT 센서 네트워크를 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('iot3@embedded.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '박라즈베리', 'https://github.com/park-raspberry', 'https://www.instagram.com/parkraspberry/', NULL, NULL, 1, '정보통신공학과 2학년입니다. 라즈베리 파이로 스마트홈 시스템을 만들고 있습니다.', NULL, 8, NOW(), NOW()),
('iot4@embedded.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '정센서', 'https://github.com/jung-sensor', 'https://twitter.com/jungsensor', NULL, NULL, 1, '전자공학과 4학년입니다. 센서 데이터 수집 및 분석 시스템을 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('iot5@embedded.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '최아두이노', 'https://github.com/choi-arduino', NULL, NULL, NULL, 1, '소프트웨어학과 3학년입니다. Arduino로 프로토타입을 만들고 있습니다.', NULL, 8, NOW(), NOW()),

-- 비학생 사용자 (25명) - 졸업생, 직장인, 프리랜서 등
('senior1@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '강시니어', 'https://github.com/kang-senior', 'https://www.linkedin.com/in/kangsenior/', NULL, NULL, 0, '졸업생입니다. 현재 IT 기업에서 백엔드 개발자로 근무하고 있습니다.', NULL, 11, NOW(), NOW()),
('senior2@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '윤리드', 'https://github.com/yoon-lead', 'https://www.linkedin.com/in/yoonlead/', 'https://twitter.com/yoonlead', NULL, 0, '졸업 후 스타트업에서 테크 리드로 일하고 있습니다.', NULL, 11, NOW(), NOW()),
('senior3@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '임매니저', 'https://github.com/lim-manager', NULL, NULL, NULL, 0, '프로젝트 매니저로 근무하며 개발팀을 이끌고 있습니다.', NULL, 11, NOW(), NOW()),
('senior4@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '한아키텍트', 'https://github.com/han-architect', 'https://www.linkedin.com/in/hanarchitect/', NULL, NULL, 0, '시스템 아키텍트로 대규모 서비스 설계를 담당하고 있습니다.', NULL, 11, NOW(), NOW()),
('senior5@company.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '신CTO', 'https://github.com/shin-cto', 'https://www.linkedin.com/in/shincto/', 'https://twitter.com/shincto', NULL, 0, '테크 스타트업의 CTO로 일하고 있습니다. 기술 전략을 수립하고 팀을 이끕니다.', NULL, 11, NOW(), NOW()),
('freelancer1@free.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '오프리', 'https://github.com/oh-free', 'https://www.instagram.com/ohfree/', NULL, NULL, 0, '프리랜서 풀스택 개발자입니다. 다양한 프로젝트를 진행하고 있습니다.', NULL, 8, NOW(), NOW()),
('freelancer2@free.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '서프론트프리', 'https://github.com/seo-front-free', NULL, NULL, NULL, 0, '프리랜서 프론트엔드 개발자로 활동 중입니다.', NULL, 8, NOW(), NOW()),
('freelancer3@free.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '권디자인', 'https://github.com/kwon-design', 'https://www.linkedin.com/in/kwondesign/', NULL, NULL, 0, 'UI/UX 디자이너 겸 프론트엔드 개발자입니다.', NULL, 8, NOW(), NOW()),
('freelancer4@free.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '조컨설턴트', 'https://github.com/jo-consultant', 'https://twitter.com/joconsultant', NULL, NULL, 0, '기술 컨설턴트로 기업의 디지털 전환을 돕고 있습니다.', NULL, 8, NOW(), NOW()),
('freelancer5@free.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '장독립', 'https://github.com/jang-independent', NULL, NULL, NULL, 0, '독립 개발자로 앱과 웹 서비스를 만들고 있습니다.', NULL, 8, NOW(), NOW()),
('startup1@founder.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '안창업', 'https://github.com/ahn-startup', 'https://www.linkedin.com/in/ahnstartup/', 'https://twitter.com/ahnstartup', NULL, 0, '스타트업 창업자입니다. 혁신적인 서비스를 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('startup2@founder.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '홍파운더', 'https://github.com/hong-founder', 'https://www.linkedin.com/in/hongfounder/', NULL, NULL, 0, 'AI 스타트업 공동 창업자입니다. 머신러닝 기반 서비스를 만들고 있습니다.', NULL, 8, NOW(), NOW()),
('startup3@founder.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '남궁CEO', 'https://github.com/namgung-ceo', NULL, NULL, NULL, 0, '핀테크 스타트업 CEO입니다. 금융 기술 혁신을 추구합니다.', NULL, 8, NOW(), NOW()),
('startup4@founder.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '황보비즈', 'https://github.com/hwangbo-biz', 'https://twitter.com/hwangbobiz', NULL, NULL, 0, '이커머스 스타트업을 운영하고 있습니다.', NULL, 8, NOW(), NOW()),
('startup5@founder.com', '$2a$10$1UoKJ5kxk8xKxKxKxKxKxe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '독고벤처', 'https://github.com/dokgo-venture', 'https://www.linkedin.com/in/dokgoventure/', NULL, NULL, 0, '에듀테크 스타트업을 창업했습니다. 교육 혁신에 관심이 많습니다.', NULL, 8, NOW(), NOW()),
('mentor1@teach.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '김멘토', 'https://github.com/kim-mentor', 'https://www.linkedin.com/in/kimmentor/', NULL, NULL, 0, '개발자 멘토로 후배들을 가르치고 있습니다. 10년차 개발자입니다.', NULL, 11, NOW(), NOW()),
('mentor2@teach.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '이선생', 'https://github.com/lee-teacher', NULL, NULL, NULL, 0, '코딩 교육 전문가입니다. 부트캠프 강사로 활동하고 있습니다.', NULL, 11, NOW(), NOW()),
('mentor3@teach.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '박코치', 'https://github.com/park-coach', 'https://twitter.com/parkcoach', NULL, NULL, 0, '기술 코치로 일하며 개발자 커리어 컨설팅을 제공합니다.', NULL, 11, NOW(), NOW()),
('mentor4@teach.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '정에듀', 'https://github.com/jung-edu', 'https://www.linkedin.com/in/jungedu/', NULL, NULL, 0, '교육 플랫폼 개발자이자 강사입니다. 온라인 교육 콘텐츠를 만듭니다.', NULL, 11, NOW(), NOW()),
('mentor5@teach.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye8xVYqp6LXVvj2fmHVfJz8xQhfLBKL.K', 'ROLE_USER', '최트레이너', 'https://github.com/choi-trainer', NULL, NULL, NULL, 0, '기업 교육 트레이너입니다. 개발팀 역량 강화 교육을 담당합니다.', NULL, 11, NOW(), NOW()),
('researcher1@lab.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '강연구', 'https://github.com/kang-research', 'https://www.linkedin.com/in/kangresearch/', 'https://twitter.com/kangresearch', NULL, 0, '인공지능 연구원입니다. 대학 연구소에서 딥러닝을 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('researcher2@lab.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '윤박사', 'https://github.com/yoon-phd', 'https://www.linkedin.com/in/yoonphd/', NULL, NULL, 0, '컴퓨터 비전 전공 박사과정입니다. 논문을 쓰고 있습니다.', NULL, 8, NOW(), NOW()),
('researcher3@lab.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '임과학자', 'https://github.com/lim-scientist', NULL, NULL, NULL, 0, '데이터 과학 연구자입니다. 빅데이터 분석 알고리즘을 개발하고 있습니다.', NULL, 8, NOW(), NOW()),
('researcher4@lab.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '한석사', 'https://github.com/han-master', 'https://twitter.com/hanmaster', NULL, NULL, 0, '보안 연구자로 취약점 분석을 연구하고 있습니다.', NULL, 8, NOW(), NOW()),
('researcher5@lab.com', '$2a$10$7J4q6Jz8xqhXvJ0x0KxXLe8xYqKxLxJxYxXxXxXxXxXxXxXxXxXxX', 'ROLE_USER', '신랩', 'https://github.com/shin-lab', 'https://www.linkedin.com/in/shinlab/', NULL, NULL, 0, '연구소 연구원으로 양자 컴퓨팅을 연구하고 있습니다.', NULL, 8, NOW(), NOW());

-- ============================================
-- POST 테이블 테스트 데이터 (20개)
-- ============================================
INSERT INTO post (member_id, title, content, view_count, like_count, dislike_count, image_urls, created_at, updated_at) VALUES
-- 백엔드 관련 게시글
(6, 'Spring Boot 3.0 신규 기능 정리', 'Spring Boot 3.0이 출시되면서 여러 신규 기능들이 추가되었습니다.\n\n주요 변경사항:\n1. Java 17 최소 요구 버전\n2. Native Image 지원 강화\n3. Observability 개선\n4. Spring MVC와 WebFlux의 성능 향상\n\n특히 Native Image 지원이 강화되어 GraalVM으로 컴파일하면 시작 속도가 획기적으로 빨라집니다. 실제로 테스트해본 결과 시작 시간이 2초에서 0.1초로 단축되었습니다.\n\n여러분은 어떤 기능이 가장 기대되시나요?', 142, 23, 1, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),

(21, 'React 18의 Concurrent Features 활용하기', 'React 18에서 추가된 Concurrent Features에 대해 정리해봤습니다.\n\nuseDeferredValue와 useTransition 훅을 활용하면 사용자 경험을 크게 개선할 수 있습니다. 특히 검색 기능 구현 시 입력은 즉시 반영하면서 검색 결과 렌더링은 지연시켜 부드러운 UX를 제공할 수 있습니다.\n\n예제 코드:\n```jsx\nconst [query, setQuery] = useState("");\nconst deferredQuery = useDeferredValue(query);\n```\n\n실무에서 적용해본 경험 공유합니다!', 89, 15, 0, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- AI/ML 관련 게시글
(38, 'ChatGPT API를 활용한 챗봇 구축 경험담', '최근 프로젝트에서 ChatGPT API를 활용해 고객 상담 챗봇을 구축했습니다.\n\n구현 과정:\n1. OpenAI API 키 발급 및 설정\n2. 프롬프트 엔지니어링으로 답변 품질 향상\n3. 대화 컨텍스트 관리를 위한 세션 구조 설계\n4. 토큰 사용량 최적화\n\n가장 어려웠던 점은 프롬프트 최적화였습니다. 시스템 프롬프트를 어떻게 작성하느냐에 따라 답변 품질이 천차만별이었습니다.\n\n결과적으로 고객 만족도가 30% 향상되었습니다!', 234, 45, 2, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

(40, 'PyTorch vs TensorFlow 실무 비교', '두 프레임워크를 실무에서 모두 사용해본 입장에서 비교해봅니다.\n\n**PyTorch의 장점:**\n- 직관적인 파이썬 코드\n- 동적 그래프로 디버깅 용이\n- 연구용으로 적합\n\n**TensorFlow의 장점:**\n- 프로덕션 배포에 강함\n- TensorFlow Serving으로 쉬운 배포\n- 모바일/임베디드 지원 우수\n\n개인적으로는 연구는 PyTorch, 프로덕션은 TensorFlow를 선호합니다. 여러분의 의견은 어떤가요?', 178, 32, 5, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- 모바일 개발 게시글
(53, 'Flutter vs React Native 2025년 선택 가이드', '크로스플랫폼 모바일 개발 프레임워크 선택이 고민되시나요?\n\n두 프레임워크를 실제 프로젝트에서 사용해본 경험을 바탕으로 비교 정리했습니다.\n\n**Flutter:**\n- 성능이 우수함 (네이티브에 가까움)\n- Material Design과 Cupertino 위젯 기본 제공\n- Dart 언어 학습 필요\n\n**React Native:**\n- JavaScript 생태계 활용 가능\n- 커뮤니티가 크고 서드파티 라이브러리 풍부\n- 네이티브 모듈 연동이 더 유연함\n\n프로젝트 특성에 따라 선택하면 됩니다!', 156, 28, 3, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),

(55, 'SwiftUI로 iOS 앱 만들기 - 입문자 가이드', 'iOS 앱 개발을 시작하시는 분들을 위한 SwiftUI 가이드입니다.\n\nSwiftUI는 선언적 UI 프레임워크로 코드가 매우 직관적입니다.\n\n간단한 리스트 뷰 예제:\n```swift\nstruct ContentView: View {\n    var body: some View {\n        List(items) { item in\n            Text(item.name)\n        }\n    }\n}\n```\n\nUIKit보다 훨씬 적은 코드로 같은 기능을 구현할 수 있습니다. 2025년 현재는 SwiftUI가 충분히 성숙해서 프로덕션에서 사용하기 좋습니다.', 95, 18, 1, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 웹 개발 게시글
(23, 'Next.js 14 App Router 완벽 가이드', 'Next.js 14의 App Router를 완전히 이해하기 위한 가이드입니다.\n\nPages Router에서 App Router로 마이그레이션하면서 배운 점들을 정리했습니다.\n\n**주요 변경점:**\n1. 파일 기반 라우팅 구조 변경\n2. Server Components 기본 사용\n3. 레이아웃 시스템 개선\n4. 데이터 페칭 방식 변경\n\nServer Components를 잘 활용하면 번들 크기를 크게 줄일 수 있습니다. 실제 프로젝트에서 40% 번들 크기 감소를 경험했습니다!', 267, 52, 4, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),

(25, 'TypeScript 5.0 새로운 기능 정리', 'TypeScript 5.0의 주요 신규 기능을 정리했습니다.\n\n**주요 기능:**\n1. Decorators 정식 지원\n2. const 타입 매개변수\n3. 성능 개선 (빌드 속도 향상)\n4. enum 개선\n\n특히 const 타입 매개변수는 타입 추론을 더욱 정확하게 만들어줍니다.\n\n```typescript\nfunction identity<const T>(value: T) {\n    return value;\n}\n```\n\n프로젝트에 바로 적용해보세요!', 124, 22, 0, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- 게임 개발 게시글
(43, 'Unity에서 최적화된 오브젝트 풀링 구현하기', '게임 개발에서 필수적인 오브젝트 풀링 패턴을 Unity에서 구현하는 방법입니다.\n\n오브젝트 풀링을 사용하면 Instantiate/Destroy 호출을 줄여 성능을 크게 향상시킬 수 있습니다.\n\n**구현 핵심:**\n- Generic 클래스로 재사용성 높이기\n- Queue 자료구조 활용\n- 초기 풀 크기 설정\n\n실제로 적용 후 FPS가 30에서 60으로 향상되었습니다. 총알, 적, 이펙트 등에 적용하면 효과가 큽니다.\n\n코드 예제와 함께 설명드립니다!', 203, 38, 2, NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),

(45, 'Unreal Engine 5 Nanite 기술 분석', 'UE5의 혁신적인 기술인 Nanite에 대해 깊이 파헤쳐봤습니다.\n\nNanite는 수십억 개의 폴리곤을 실시간으로 렌더링할 수 있게 해주는 가상화된 지오메트리 시스템입니다.\n\n**핵심 원리:**\n1. LOD 자동 생성 및 전환\n2. 클러스터 기반 렌더링\n3. 가시성 버퍼 활용\n\n실제 프로젝트에 적용해본 결과, 고퀄리티 에셋을 최적화 없이 바로 사용할 수 있어 작업 효율이 크게 향상되었습니다.', 189, 35, 3, NULL, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),

-- 보안 관련 게시글
(33, 'JWT 인증 보안 체크리스트', 'JWT를 사용한 인증 시스템을 구축할 때 반드시 확인해야 할 보안 사항들입니다.\n\n**필수 체크 항목:**\n1. Secret Key는 충분히 길고 복잡하게 (최소 256비트)\n2. HTTPS 필수 사용\n3. Refresh Token은 별도 저장소에 안전하게 보관\n4. 만료 시간 적절히 설정 (Access: 15분, Refresh: 7일)\n5. 민감한 정보는 JWT에 포함하지 않기\n\n실무에서 자주 발생하는 보안 취약점과 대응 방법도 함께 정리했습니다.', 312, 58, 1, NULL, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),

(34, 'SQL Injection 공격과 방어 기법', '여전히 많이 발생하는 SQL Injection 공격에 대해 정리했습니다.\n\n**공격 예시:**\n```sql\nSELECT * FROM users WHERE username = \'\' OR \'1\'=\'1\' --\n```\n\n**방어 방법:**\n1. Prepared Statement 사용 (필수!)\n2. ORM 프레임워크 활용\n3. 입력값 검증 및 이스케이핑\n4. 최소 권한 원칙 적용\n\nPrepared Statement만 제대로 사용해도 대부분의 SQL Injection을 막을 수 있습니다. 절대 문자열 연결로 쿼리를 만들지 마세요!', 278, 47, 2, NULL, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY)),

-- 데이터 분석 게시글
(48, 'Pandas로 대용량 데이터 효율적으로 처리하기', 'Pandas로 큰 데이터를 다룰 때 성능을 최적화하는 방법입니다.\n\n**최적화 기법:**\n1. 데이터 타입 최적화 (int64 → int32)\n2. Chunksize로 나눠서 읽기\n3. 불필요한 컬럼 제외하고 읽기\n4. 벡터화 연산 활용\n\n```python\ndf = pd.read_csv(\'large.csv\', \n                 usecols=[\'col1\', \'col2\'],\n                 dtype={\'col1\': \'int32\'})\n```\n\n이 방법들로 10GB 데이터를 8GB 메모리에서 처리할 수 있었습니다!', 145, 27, 1, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),

(50, 'Matplotlib 시각화 팁 모음', '데이터 시각화를 더 아름답게 만드는 Matplotlib 팁들을 모았습니다.\n\n**유용한 팁:**\n1. seaborn 스타일 적용하기\n2. 폰트 크기 일괄 조정\n3. 그리드 스타일 커스터마이징\n4. 컬러맵 효과적으로 사용하기\n\n```python\nplt.style.use(\'seaborn-v0_8\')\nplt.rcParams[\'font.size\'] = 12\n```\n\n이것만 알아도 훨씬 깔끔한 그래프를 그릴 수 있습니다. 발표 자료나 논문에 바로 사용 가능한 퀄리티!', 98, 19, 0, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 클라우드/인프라 게시글
(58, 'Docker Compose로 개발 환경 구축하기', '로컬 개발 환경을 Docker Compose로 표준화하는 방법입니다.\n\n프로젝트마다 환경 설정이 달라 고생하셨나요? Docker Compose를 사용하면 모든 팀원이 동일한 환경에서 개발할 수 있습니다.\n\n**docker-compose.yml 예시:**\n```yaml\nservices:\n  backend:\n    build: ./backend\n    ports:\n      - "8080:8080"\n  db:\n    image: mysql:8.0\n    environment:\n      MYSQL_ROOT_PASSWORD: password\n```\n\n한 번 설정해두면 `docker-compose up` 명령어 하나로 전체 환경이 실행됩니다!', 167, 31, 2, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),

(60, 'Kubernetes 기초부터 실전까지', 'Kubernetes 입문자를 위한 완벽 가이드입니다.\n\n처음에는 어렵게 느껴지지만, 핵심 개념만 이해하면 쉽습니다.\n\n**핵심 개념:**\n1. Pod: 가장 작은 배포 단위\n2. Service: Pod 접근을 위한 네트워크\n3. Deployment: Pod 배포 관리\n4. ConfigMap/Secret: 설정 관리\n\n간단한 웹 앱을 K8s에 배포하는 과정을 단계별로 설명합니다. 로컬에서 Minikube로 실습해볼 수 있습니다!', 223, 42, 3, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 알고리즘/자료구조 게시글
(29, '코딩 테스트 단골 알고리즘 정리', '코딩 테스트에 자주 나오는 알고리즘과 풀이 패턴을 정리했습니다.\n\n**반드시 알아야 할 알고리즘:**\n1. 투 포인터\n2. 슬라이딩 윈도우\n3. 이진 탐색\n4. BFS/DFS\n5. 동적 계획법\n6. 그리디\n\n각 알고리즘별로 대표 문제와 템플릿 코드를 정리했습니다. 이것만 마스터하면 웬만한 코딩 테스트는 통과할 수 있습니다.\n\n백준, 프로그래머스 문제 링크도 함께 첨부합니다!', 445, 89, 5, NULL, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- IoT/임베디드 게시글
(62, '라즈베리 파이로 IoT 프로젝트 시작하기', '라즈베리 파이를 활용한 IoT 프로젝트 입문 가이드입니다.\n\n**프로젝트 아이디어:**\n1. 스마트 온도계 (DHT22 센서)\n2. 움직임 감지 카메라\n3. 홈 자동화 시스템\n4. 날씨 정보 디스플레이\n\n가장 기본적인 LED 제어부터 시작해서, 센서 데이터를 수집하고 클라우드로 전송하는 방법까지 설명합니다.\n\nPython으로 GPIO를 제어하는 것이 생각보다 쉽습니다!', 134, 25, 1, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- 기타 개발 관련
(17, 'Git 고급 기능 활용하기', '실무에서 유용한 Git 고급 기능들을 정리했습니다.\n\n**유용한 명령어:**\n1. git rebase -i (커밋 히스토리 정리)\n2. git bisect (버그 발생 커밋 찾기)\n3. git stash (임시 저장)\n4. git cherry-pick (특정 커밋만 가져오기)\n\n특히 rebase interactive는 커밋 메시지를 정리하거나 여러 커밋을 합칠 때 매우 유용합니다.\n\n```bash\ngit rebase -i HEAD~3\n```\n\n깔끔한 커밋 히스토리는 팀 협업의 기본입니다!', 198, 37, 2, NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),

(70, '효율적인 코드 리뷰 문화 만들기', '좋은 코드 리뷰 문화를 만들기 위한 가이드입니다.\n\n**코드 리뷰 원칙:**\n1. 비난이 아닌 학습의 기회\n2. 구체적인 피드백 제공\n3. 긍정적인 부분도 언급\n4. 자동화 가능한 것은 린터/포매터로\n\n코드 리뷰는 단순히 버그를 찾는 것이 아니라 팀의 코드 품질을 높이고 지식을 공유하는 과정입니다.\n\n실제 팀에서 적용한 코드 리뷰 체크리스트도 공유합니다!', 256, 48, 3, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY));

-- ============================================
-- POST_TAG 테이블 테스트 데이터
-- ============================================
-- 태그 ID 참고:
-- 1: 풀스택, 2: 웹개발, 3: 프론트엔드, 4: 백엔드, 5: 모바일 앱 개발
-- 7: 알고리즘•자료구조, 8: 데이터베이스, 9: 데브옵스•인프라
-- 15: AI활용, 16: AI, 17: 머신러닝•딥러닝
-- 23: 데이터 분석, 24: 데이터 엔지니어링
-- 27: 게임 프로그래밍
-- 32: 보안, 33: 사이버보안
-- 44: 임베디드•IoT

INSERT INTO post_tag (post_id, tag_id) VALUES
-- Post 1: Spring Boot (백엔드, 웹개발)
(1, 4), (1, 2),
-- Post 2: React (프론트엔드, 웹개발)
(2, 3), (2, 2),
-- Post 3: ChatGPT (AI, AI활용)
(3, 16), (3, 15),
-- Post 4: PyTorch vs TensorFlow (머신러닝, AI)
(4, 17), (4, 16),
-- Post 5: Flutter vs React Native (모바일)
(5, 5),
-- Post 6: SwiftUI (모바일)
(6, 5),
-- Post 7: Next.js (프론트엔드, 웹개발)
(7, 3), (7, 2),
-- Post 8: TypeScript (프론트엔드, 프로그래밍 언어)
(8, 3), (8, 6),
-- Post 9: Unity (게임 프로그래밍)
(9, 27),
-- Post 10: Unreal Engine (게임 프로그래밍)
(10, 27),
-- Post 11: JWT (보안, 백엔드)
(11, 32), (11, 4),
-- Post 12: SQL Injection (보안, 데이터베이스)
(12, 32), (12, 8),
-- Post 13: Pandas (데이터 분석)
(13, 23),
-- Post 14: Matplotlib (데이터 분석)
(14, 23),
-- Post 15: Docker Compose (데브옵스)
(15, 9),
-- Post 16: Kubernetes (데브옵스)
(16, 9),
-- Post 17: 알고리즘 (알고리즘•자료구조)
(17, 7),
-- Post 18: 라즈베리 파이 (임베디드•IoT)
(18, 44),
-- Post 19: Git (개발•프로그래밍 기타)
(19, 14),
-- Post 20: 코드 리뷰 (개발•프로그래밍 기타)
(20, 14);

-- ============================================
-- PROJECT 테이블 테스트 데이터 (10개)
-- ============================================
INSERT INTO project (member_id, project_title, project_description, is_recruiting, view_count, image_urls, created_at, updated_at) VALUES
-- 웹 개발 프로젝트
(7, '헬스케어 서비스 웹 앱 개발 팀원 모집', '건강 관리를 위한 웹 애플리케이션을 함께 만들 팀원을 모집합니다!\n\n**프로젝트 개요:**\n운동 기록, 식단 관리, 건강 데이터 시각화 기능을 제공하는 종합 헬스케어 플랫폼입니다.\n\n**기술 스택:**\n- Frontend: React, TypeScript, TailwindCSS\n- Backend: Spring Boot, MySQL\n- 배포: AWS EC2, RDS\n\n**모집 인원:**\n- 프론트엔드 개발자 1명\n- 백엔드 개발자 1명\n- UI/UX 디자이너 1명\n\n**활동 기간:** 3개월 (2025.01 ~ 2025.03)\n**예상 시간:** 주 10-15시간\n\n함께 성장하며 포트폴리오에 남을 프로젝트를 만들어봐요!', 1, 87, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),

-- AI 프로젝트
(38, 'AI 챗봇 기반 학습 도우미 앱 개발', '학생들의 학습을 돕는 AI 챗봇 서비스를 개발할 팀원을 찾습니다.\n\n**프로젝트 목표:**\nGPT API를 활용해 질문-답변, 문제 생성, 학습 계획 수립 등을 지원하는 학습 도우미를 만듭니다.\n\n**기술 스택:**\n- AI: OpenAI GPT API\n- Backend: FastAPI (Python)\n- Frontend: Next.js\n- DB: PostgreSQL\n\n**모집 인원:**\n- AI/ML 개발자 1명\n- 백엔드 개발자 1명\n- 프론트엔드 개발자 1명\n\n**우대 사항:**\n- 프롬프트 엔지니어링 경험\n- LangChain 사용 경험\n\n교육 분야에 관심 있는 분들의 많은 지원 바랍니다!', 1, 142, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 모바일 앱 프로젝트
(53, '캠퍼스 중고거래 앱 "캠퍼스 마켓" 개발', '우리 학교 학생들을 위한 중고거래 플랫폼을 만듭니다!\n\n**프로젝트 설명:**\n학교 이메일 인증으로 안전한 거래 환경을 제공하고, 캠퍼스 내 만남 장소 추천 등의 특화 기능을 제공합니다.\n\n**기술 스택:**\n- Mobile: Flutter\n- Backend: Node.js, Express\n- DB: MongoDB\n- 인증: Firebase Auth\n\n**모집 인원:**\n- Flutter 개발자 2명\n- 백엔드 개발자 1명\n\n**활동 기간:** 4개월\n\n실제 서비스 런칭을 목표로 합니다. 창업에 관심 있으신 분 환영!', 1, 103, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 게임 개발 프로젝트
(43, '2D 로그라이크 게임 개발 프로젝트', 'Unity로 2D 로그라이크 게임을 만들 팀원을 모집합니다.\n\n**게임 컨셉:**\n- 장르: 로그라이크 액션\n- 스타일: 픽셀아트\n- 플랫폼: PC (Steam 출시 목표)\n\n**기술 스택:**\n- 엔진: Unity 2022 LTS\n- 언어: C#\n\n**모집 인원:**\n- 게임 프로그래머 2명\n- 2D 아티스트 1명\n- 사운드 디자이너 1명 (선택)\n\n**필요 역량:**\n- Unity 기본 사용 경험\n- C# 프로그래밍\n- 게임 제작 열정!\n\n**활동 기간:** 6개월\n\n함께 스팀에 게임을 출시해봐요!', 1, 156, NULL, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),

-- 데이터 분석 프로젝트
(48, '공공데이터를 활용한 지역 분석 프로젝트', '공공데이터 포털의 데이터를 활용해 우리 지역을 분석하는 프로젝트입니다.\n\n**분석 주제:**\n- 지역별 인구 변화 추이\n- 상권 분석 및 예측\n- 교통 패턴 분석\n- 대기질 및 환경 데이터 분석\n\n**기술 스택:**\n- Python (Pandas, NumPy, Scikit-learn)\n- 시각화: Matplotlib, Seaborn, Plotly\n- 대시보드: Streamlit\n\n**모집 인원:**\n- 데이터 분석가 2명\n- 웹 개발자 1명 (대시보드 개발)\n\n**활동 기간:** 2개월\n\n데이터 분석 포트폴리오를 만들고 싶은 분들 환영합니다!', 1, 78, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- IoT 프로젝트
(62, '스마트 팜 IoT 시스템 구축 프로젝트', '농업 자동화를 위한 IoT 시스템을 만드는 프로젝트입니다.\n\n**프로젝트 내용:**\n라즈베리 파이와 각종 센서를 활용해 온도, 습도, 토양 수분을 모니터링하고 자동으로 제어하는 시스템을 구축합니다.\n\n**기술 스택:**\n- 하드웨어: Raspberry Pi 4, Arduino\n- 센서: DHT22, 토양 수분 센서, 조도 센서\n- Backend: Python Flask\n- Frontend: React\n- DB: InfluxDB (시계열 데이터)\n\n**모집 인원:**\n- 임베디드 개발자 1명\n- 백엔드 개발자 1명\n- 프론트엔드 개발자 1명\n\n실제 작동하는 하드웨어 프로젝트를 경험해보세요!', 1, 92, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),

-- 블록체인 프로젝트
(42, 'NFT 아트 갤러리 플랫폼 개발', 'Web3 기반 NFT 아트 마켓플레이스를 만듭니다.\n\n**프로젝트 설명:**\n예술가들이 자신의 작품을 NFT로 발행하고 거래할 수 있는 플랫폼입니다.\n\n**기술 스택:**\n- 블록체인: Ethereum (Sepolia Testnet)\n- 스마트 컨트랙트: Solidity\n- Frontend: React, Web3.js\n- Backend: Node.js\n- 저장소: IPFS\n\n**모집 인원:**\n- 블록체인 개발자 1명\n- 프론트엔드 개발자 1명\n- 백엔드 개발자 1명\n\n**우대 사항:**\n- Solidity 경험\n- Web3 생태계 이해\n\n**활동 기간:** 3개월\n\nWeb3 트렌드를 직접 경험해보세요!', 0, 234, NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- 보안 프로젝트
(33, '취약점 스캐너 도구 개발 프로젝트', '웹 애플리케이션의 보안 취약점을 자동으로 탐지하는 도구를 개발합니다.\n\n**개발 내용:**\n- SQL Injection 탐지\n- XSS 취약점 검사\n- 인증/인가 검증\n- 보안 헤더 체크\n- 자동화된 보고서 생성\n\n**기술 스택:**\n- Python\n- Selenium, BeautifulSoup\n- Flask (웹 인터페이스)\n\n**모집 인원:**\n- 보안 전문가 1명\n- Python 개발자 1명\n- 프론트엔드 개발자 1명\n\n**활동 기간:** 3개월\n\n보안에 관심 있는 화이트햇 해커 지망생 환영!', 1, 118, NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- 교육 플랫폼 프로젝트
(95, '실시간 코딩 교육 플랫폼 "CodeTogether"', '온라인으로 실시간 코딩 교육이 가능한 플랫폼을 만듭니다.\n\n**주요 기능:**\n- 실시간 코드 공유 및 편집\n- 화상 통화 기능\n- 코드 실행 환경 제공\n- 과제 제출 및 피드백 시스템\n\n**기술 스택:**\n- Frontend: Vue.js, Monaco Editor\n- Backend: Spring Boot, WebSocket\n- 실시간 통신: Socket.io\n- 화상: WebRTC\n- 코드 실행: Docker 샌드박스\n\n**모집 인원:**\n- 풀스택 개발자 2명\n- 백엔드 개발자 1명\n- DevOps 엔지니어 1명\n\n**활동 기간:** 4개월\n\n실제 서비스 런칭이 목표입니다!', 1, 167, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- 소셜 네트워크 프로젝트
(25, '개발자 커뮤니티 "DevCircle" 구축', '개발자들을 위한 SNS 플랫폼을 만듭니다.\n\n**차별화 포인트:**\n- 코드 스니펫 공유 최적화\n- 기술 스택 기반 매칭\n- 스터디/프로젝트 팀 빌딩\n- 포트폴리오 자동 생성\n\n**기술 스택:**\n- Frontend: Next.js, TypeScript\n- Backend: NestJS, GraphQL\n- DB: PostgreSQL, Redis\n- 검색: Elasticsearch\n- 인프라: AWS, Docker\n\n**모집 인원:**\n- 프론트엔드 개발자 2명\n- 백엔드 개발자 2명\n- UI/UX 디자이너 1명\n\n**활동 기간:** 6개월\n\n대규모 프로젝트 경험을 쌓고 싶은 분들 환영합니다!', 1, 203, NULL, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY));

-- ============================================
-- PROJECT_TAG 테이블 테스트 데이터
-- ============================================
INSERT INTO project_tag (project_id, tag_id) VALUES
-- Project 1: 헬스케어 웹앱 (풀스택, 웹개발)
(1, 1), (1, 2),
-- Project 2: AI 챗봇 (AI, 백엔드)
(2, 16), (2, 4),
-- Project 3: 중고거래 앱 (모바일)
(3, 5),
-- Project 4: 2D 게임 (게임 프로그래밍)
(4, 27),
-- Project 5: 데이터 분석 (데이터 분석)
(5, 23),
-- Project 6: 스마트 팜 (임베디드•IoT)
(6, 44),
-- Project 7: NFT 플랫폼 (블록체인, 웹개발)
(7, 41), (7, 2),
-- Project 8: 보안 스캐너 (보안)
(8, 32),
-- Project 9: 교육 플랫폼 (풀스택, 웹개발)
(9, 1), (9, 2),
-- Project 10: 개발자 SNS (풀스택, 웹개발)
(10, 1), (10, 2);

-- ============================================
-- PROJECT_MEMBER 테이블 테스트 데이터
-- ============================================
-- 슬롯 구조: 각 프로젝트의 리더(member_id 있음)와 모집 중인 포지션(member_id NULL)
INSERT INTO project_member (project_member_id, project_id, member_id, role, part) VALUES
-- Project 1: 헬스케어 웹앱 (리더: member 7, 모집: 프론트 1, 백엔드 1, 디자인 1)
(1, 1, 7, 'LEADER', 'FRONTEND'),
(2, 1, NULL, 'MEMBER', 'FRONTEND'),
(3, 1, NULL, 'MEMBER', 'BACKEND'),
(4, 1, NULL, 'MEMBER', 'DESIGN'),

-- Project 2: AI 챗봇 (리더: member 38, 모집: AI 1, 백엔드 1, 프론트 1)
(5, 2, 38, 'LEADER', 'AI'),
(6, 2, NULL, 'MEMBER', 'AI'),
(7, 2, NULL, 'MEMBER', 'BACKEND'),
(8, 2, NULL, 'MEMBER', 'FRONTEND'),

-- Project 3: 중고거래 앱 (리더: member 53, 모집: Flutter(프론트) 2, 백엔드 1)
(9, 3, 53, 'LEADER', 'FRONTEND'),
(10, 3, NULL, 'MEMBER', 'FRONTEND'),
(11, 3, NULL, 'MEMBER', 'FRONTEND'),
(12, 3, NULL, 'MEMBER', 'BACKEND'),

-- Project 4: 2D 게임 (리더: member 43, 모집: 프로그래머 2, 아티스트 1, 사운드 1)
(13, 4, 43, 'LEADER', 'FRONTEND'),
(14, 4, NULL, 'MEMBER', 'FRONTEND'),
(15, 4, NULL, 'MEMBER', 'FRONTEND'),
(16, 4, NULL, 'MEMBER', 'DESIGN'),
(17, 4, NULL, 'MEMBER', 'DESIGN'),

-- Project 5: 공공데이터 분석 (리더: member 48, 모집: 데이터 분석 2, 웹개발 1)
(18, 5, 48, 'LEADER', 'AI'),
(19, 5, NULL, 'MEMBER', 'AI'),
(20, 5, NULL, 'MEMBER', 'AI'),
(21, 5, NULL, 'MEMBER', 'FRONTEND'),

-- Project 6: 스마트 팜 IoT (리더: member 62, 모집: 임베디드 1, 백엔드 1, 프론트 1)
(22, 6, 62, 'LEADER', 'ETC'),
(23, 6, NULL, 'MEMBER', 'ETC'),
(24, 6, NULL, 'MEMBER', 'BACKEND'),
(25, 6, NULL, 'MEMBER', 'FRONTEND'),

-- Project 7: NFT 플랫폼 (리더: member 42, 모집: 블록체인 1, 프론트 1, 백엔드 1)
(26, 7, 42, 'LEADER', 'BACKEND'),
(27, 7, NULL, 'MEMBER', 'BACKEND'),
(28, 7, NULL, 'MEMBER', 'FRONTEND'),
(29, 7, NULL, 'MEMBER', 'BACKEND'),

-- Project 8: 취약점 스캐너 (리더: member 33, 모집: 보안 1, Python 1, 프론트 1)
(30, 8, 33, 'LEADER', 'BACKEND'),
(31, 8, NULL, 'MEMBER', 'BACKEND'),
(32, 8, NULL, 'MEMBER', 'BACKEND'),
(33, 8, NULL, 'MEMBER', 'FRONTEND'),

-- Project 9: 코딩 교육 플랫폼 (리더: member 95, 모집: 풀스택 2, 백엔드 1, DevOps 1)
(34, 9, 95, 'LEADER', 'FRONTEND'),
(35, 9, NULL, 'MEMBER', 'FRONTEND'),
(36, 9, NULL, 'MEMBER', 'FRONTEND'),
(37, 9, NULL, 'MEMBER', 'BACKEND'),
(38, 9, NULL, 'MEMBER', 'ETC'),

-- Project 10: 개발자 SNS (리더: member 25, 모집: 프론트 2, 백엔드 2, 디자이너 1)
(39, 10, 25, 'LEADER', 'FRONTEND'),
(40, 10, NULL, 'MEMBER', 'FRONTEND'),
(41, 10, NULL, 'MEMBER', 'FRONTEND'),
(42, 10, NULL, 'MEMBER', 'BACKEND'),
(43, 10, NULL, 'MEMBER', 'BACKEND'),
(44, 10, NULL, 'MEMBER', 'DESIGN');

-- ============================================
-- 삽입 완료 메시지
-- ============================================
SELECT 'Test data insertion completed! 100 members, 20 posts, 10 projects, and project members added.' AS message;