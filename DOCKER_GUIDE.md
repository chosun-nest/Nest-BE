# Docker Compose 사용 가이드

## 📋 개요

이 프로젝트는 Docker Compose를 사용하여 Spring Boot 백엔드 서버와 MySQL 데이터베이스를 함께 실행할 수 있습니다.

---

## 🚀 빠른 시작

### 1. 사전 준비

- Docker 및 Docker Compose 설치 필요
- Java 17 설치 (로컬 빌드 시)

### 2. 환경 변수 설정 (선택사항)

```bash
# .env 파일 생성 (필요시)
cp .env.example .env
# .env 파일을 열어 필요한 값 수정
```

### 3. 애플리케이션 빌드

```bash
# Gradle 빌드 (jar 파일 생성)
./gradlew clean build -x test
```

### 4. Docker Compose 실행

```bash
# 백그라운드에서 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 특정 서비스 로그만 보기
docker-compose logs -f backend
docker-compose logs -f mysql
```

### 5. 서비스 확인

- **백엔드 서버**: http://localhost:6030
- **Swagger UI**: http://localhost:6030/swagger-ui/index.html
- **MySQL**: localhost:3306

---

## 🛠 주요 명령어

### 시작 및 종료

```bash
# 서비스 시작 (빌드 후 실행)
docker-compose up -d

# 서비스 중지
docker-compose stop

# 서비스 중지 및 컨테이너 삭제
docker-compose down

# 서비스 중지 + 볼륨 삭제 (데이터베이스 초기화)
docker-compose down -v
```

### 재빌드 및 재시작

```bash
# 코드 수정 후 재빌드
./gradlew clean build -x test
docker-compose up -d --build

# 특정 서비스만 재시작
docker-compose restart backend
```

### 로그 및 디버깅

```bash
# 전체 로그 보기
docker-compose logs

# 실시간 로그 (tail -f)
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs backend

# 컨테이너 접속
docker-compose exec backend sh
docker-compose exec mysql bash
```

### MySQL 접속

```bash
# Docker 컨테이너 내부에서 접속
docker-compose exec mysql mysql -u nest -p nest_db

# 로컬에서 접속 (MySQL 클라이언트 설치 필요)
mysql -h localhost -P 3306 -u nest -p nest_db
# 비밀번호: nest
```

---

## 📂 구조

```
.
├── docker-compose.yml      # Docker Compose 설정
├── Dockerfile              # 백엔드 이미지 빌드
├── .dockerignore           # Docker 빌드 제외 파일
├── .env.example            # 환경변수 예시
├── build/libs/*.jar        # 빌드된 JAR 파일
└── uploaded-images/        # 업로드된 이미지 저장 (볼륨)
```

---

## ⚙️ Docker Compose 구성

### Services

1. **mysql**: MySQL 8.0 데이터베이스
   - 포트: 3306
   - 데이터베이스: nest_db
   - 사용자: nest / nest
   - 볼륨: mysql-data (영속성)

2. **backend**: Spring Boot 애플리케이션
   - 포트: 6030
   - MySQL 연결: mysql:3306
   - 이미지 저장: ./uploaded-images

### Networks

- **nest-network**: 브리지 네트워크로 서비스 간 통신

### Volumes

- **mysql-data**: MySQL 데이터 영속화
- **uploaded-images**: 업로드된 파일 저장

---

## 🔧 환경변수

### 데이터베이스 설정

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/nest_db
SPRING_DATASOURCE_USERNAME: nest
SPRING_DATASOURCE_PASSWORD: nest
```

### JWT 설정

```yaml
JWT_SECRET: your-secret-key
JWT_ACCESS_TOKEN_EXPIRATION: 3600000
JWT_REFRESH_TOKEN_EXPIRATION: 604800000
```

---

## 🐛 문제 해결

### 1. MySQL 연결 실패

```bash
# MySQL 컨테이너 상태 확인
docker-compose ps

# MySQL 로그 확인
docker-compose logs mysql

# MySQL이 완전히 시작되지 않았을 수 있음 (재시작)
docker-compose restart backend
```

### 2. 포트 충돌

```bash
# 이미 사용 중인 포트 확인
lsof -i :6030  # 백엔드
lsof -i :3306  # MySQL

# docker-compose.yml에서 포트 변경
ports:
  - "6031:6030"  # 호스트:컨테이너
```

### 3. 빌드 실패

```bash
# Gradle 캐시 삭제 후 재빌드
./gradlew clean
./gradlew build -x test

# Docker 이미지 강제 재빌드
docker-compose build --no-cache
```

### 4. 데이터베이스 초기화

```bash
# 모든 컨테이너 및 볼륨 삭제
docker-compose down -v

# 재시작
docker-compose up -d
```

---

## 🚢 프로덕션 배포

### 1. JAR 파일 빌드

```bash
./gradlew clean build -x test
```

### 2. 서버에 파일 업로드

```bash
# 필요한 파일만 업로드
- docker-compose.yml
- Dockerfile
- build/libs/*.jar
- .env (환경변수)
```

### 3. 서버에서 실행

```bash
# 서버 접속 후
docker-compose up -d

# 로그 확인
docker-compose logs -f
```

### 4. 업데이트 배포

```bash
# 새 버전 배포
./gradlew clean build -x test  # 로컬에서 빌드
# 서버에 jar 파일 업로드
docker-compose up -d --build  # 서버에서 실행
```

---

## 📊 모니터링

```bash
# 컨테이너 리소스 사용량 확인
docker stats

# 실행 중인 컨테이너 확인
docker-compose ps

# 헬스 체크
curl http://localhost:6030/actuator/health  # (Actuator 설정 시)
```

---

## 🔒 보안 주의사항

1. **프로덕션 환경**에서는 반드시 다음을 변경하세요:
   - JWT_SECRET
   - MYSQL_ROOT_PASSWORD
   - MYSQL_PASSWORD
   - 메일 비밀번호

2. **.env 파일**을 Git에 커밋하지 마세요:
   ```bash
   echo ".env" >> .gitignore
   ```

3. **외부 노출**을 최소화하세요:
   ```yaml
   # MySQL 포트를 외부에 노출하지 않기
   # ports:
   #   - "3306:3306"  # 이 줄 주석 처리
   ```

---

## 📞 문의

문제가 발생하면 팀에 문의하세요!