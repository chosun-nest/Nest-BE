# NEST 백엔드 (Spring Boot)

NEST는 캠퍼스/커뮤니티용 프로젝트 모집 및 콘텐츠 플랫폼의 Spring Boot 백엔드입니다. JWT 인증, 회원 프로필, 프로젝트 모집글(이미지 포함/미포함), 프로젝트 지원/멤버십 관리, 일반 게시글/댓글/리액션, 태그/관심/기술스택/학과, 팔로우, 공지, 간단한 채팅방 등을 제공합니다. Swagger UI로 API를 탐색할 수 있습니다.

**주요 특징**
- JWT 기반 인증/인가, 토큰 재발급, 이메일 기반 비밀번호 재설정
- 프로젝트 모집글: JSON(v1) + 이미지 포함 Multipart(v2)
- 프로젝트 지원서/멤버십 역할 관리
- 게시글/댓글/리액션/공지
- 태그, 관심사, 기술스택, 학과 관리
- 프로필 이미지 업로드 및 정적 파일 제공
- Swagger UI, 도메인별 예외 처리 일원화

**기술 스택**
- Java 17, Spring Boot 3.4.x
- Spring Web, Spring Security, Spring Data JPA
- MySQL 8.x
- JJWT 0.11.x
- Springdoc OpenAPI (Swagger UI)
- Gradle

**포트/접속 경로**
- 서버 포트: `6030`
- Swagger UI: `http://localhost:6030/swagger-ui.html`
- OpenAPI JSON: `http://localhost:6030/v3/api-docs`
- 정적 이미지: `http://localhost:6030/uploaded-images/...`

## 로컬 실행

사전 준비
- JDK 17+
- MySQL 8+ (로컬 DB `nest_db` 권장)
- Gradle Wrapper 포함

DB 설정
- `src/main/resources/application.yaml`의 `spring.datasource.*` 값을 환경에 맞게 수정합니다.
- 기본값(레포 기준):
  - URL: `jdbc:mysql://localhost:3306/nest_db`
  - Username: `nest`
  - Password: `nest`

애플리케이션 설정
- 파일: `src/main/resources/application.yaml`
- 주요 키:
  - `server.port`: 기본 `6030`
  - `spring.datasource.*`: MySQL 연결 정보
  - `jwt.secret`, `jwt.access-token-expiration`, `jwt.refresh-token-expiration`
  - `spring.mail.*`: 이메일 전송 설정(비밀번호 재설정 등)

실행
- `./gradlew bootRun`
- 브라우저: `http://localhost:6030`

Jar 빌드
- `./gradlew bootJar`
- 산출물: `build/libs/*.jar`

## Docker 실행

- 스크립트 실행: `./docker-build.sh`
  - 포트 `6030` 바인딩, `./uploaded-images`를 컨테이너 `/app/uploaded-images`로 볼륨 마운트(이미지 영속화)
- 내부 동작
  - `bootJar` → jar 빌드
  - `docker build` → 이미지 태깅: `nest-be-spring-boot-6030-image`
  - `docker run -p 6030:6030 -v $(pwd)/uploaded-images:/app/uploaded-images`

## 인증

- 엔드포인트: `POST /api/v1/auth/signup`, `POST /api/v1/auth/login`, `POST /api/v1/auth/refresh`
- 요청 시 헤더: `Authorization: Bearer {access_token}`
- 토큰 간단 검증: `GET /api/v1/auth/me` (토큰에서 memberId 반환)

비밀번호 재설정
- `POST /api/v1/auth/password-reset-link-request`
- `POST /api/v1/auth/password-reset`

## 프로젝트 모듈

컨트롤러
- v1(JSON): `src/main/java/com/virtukch/nest/project/controller/ProjectController.java`
- v2(이미지 포함 Multipart): `src/main/java/com/virtukch/nest/project/controller/ProjectV2Controller.java`

주요 엔드포인트
- 생성(JSON): `POST /api/v1/projects/new`
  - Body: `ProjectRequestDto`
    - `projectTitle`(필수), `projectDescription`, `isRecruiting`,
      `tags: string[]`,
      `partCounts: { FRONTEND|BACKEND|DESIGNER|PLANNER|DEVOPS|FULLSTACK|ANDROID|IOS: number }`,
      `creatorPart`, `creatorRole`(기본 `LEADER`), `membersToRemove`
- 생성(Multipart 이미지 포함): `POST /api/v2/projects`
  - Form-data: `ProjectWithImagesRequestDto`
    - v1과 동일한 텍스트 필드 + `images: file[]`
- 수정 v1(JSON): `PATCH /api/v1/projects/{projectId}`
- 수정 v2(Multipart): `PATCH /api/v2/projects/{projectId}`
- 삭제: `DELETE /api/v1/projects/{projectId}`
- 상세: `GET /api/v1/projects/{projectId}`
- 목록: `GET /api/v1/projects` (공개)
- 검색: `GET /api/v1/projects/search`
- 모집 파트 enum: `GET /api/v1/projects/members` (허용 파트 문자열 목록)

주의
- 생성/수정/삭제는 유효한 JWT가 필요합니다(없거나 만료/위조 시 401).
- 작성자가 아닌 경우 수정/삭제 시 403 반환될 수 있습니다.

## 게시글/댓글

- 게시글 v1: `src/main/java/com/virtukch/nest/post/controller/PostController.java`
- 게시글 v2(이미지): `src/main/java/com/virtukch/nest/post/controller/PostV2Controller.java`
- 댓글: `src/main/java/com/virtukch/nest/comment/controller/CommentController.java`
- 공개 조회 허용, 쓰기 작업은 JWT 필요.

## 회원

- 컨트롤러: `src/main/java/com/virtukch/nest/member/controller/MemberController.java`
- 프로필 조회: `GET /api/v1/members/{memberId}`
- 내 정보: `GET /api/v1/members/me`
- 정보 수정: `PATCH /api/v1/members/me`
- 비밀번호 변경: `PATCH /api/v1/members/me/password`
- 비밀번호 확인: `POST /api/v1/members/check-password`
- 프로필 이미지 업로드: `POST /api/v1/members/me/image` (multipart, key=`file`)

## 태그/관심사/기술스택/학과

- 태그: `GET /api/v1/tags/**`, 즐겨찾기 `/api/v1/tag-favorites`
- 관심사: `GET /api/v1/interests`
- 기술스택: `GET /api/v1/tech-stacks`
- 학과: `GET /api/v1/departments`

## 프로젝트 지원/멤버십

- 지원 컨트롤러: `src/main/java/com/virtukch/nest/project_application/controller/ProjectApplicationController.java`
- 역할/enum: `src/main/java/com/virtukch/nest/project_member/model/ProjectMember.java`
- 최대 인원/역할 제약, 작성자 전용 승인 등 비즈니스 규칙이 적용됩니다(권한 위반 시 403).

## 보안

- 설정: `src/main/java/com/virtukch/nest/auth/security/SecurityConfig.java`
- JWT 필터: `JwtAuthenticationFilter` (헤더 `Authorization: Bearer {token}`)
- 공개 엔드포인트(요약):
  - `/api/v1/auth/**`, `/api/v1/tech-stacks`, `/api/v1/interests`, `/api/v1/departments`, `/api/v1/tags/**`,
    `/api/v1/posts/search`, `/api/v1/notices/**`, `/api/v1/projects`, `/api/v1/projects/search`,
    GET: `/api/v1/posts`, `/api/v2/posts`, `/uploaded-images/**`, `/api/v1/projects`, `/api/v2/projects`
- 그 외 모든 엔드포인트는 인증 필요.

## 정적 이미지

- 레포 루트의 `uploaded-images/`에 저장되고 `/uploaded-images/**`로 서빙됩니다.
- Docker 실행 시 볼륨 마운트로 컨테이너 재시작 후에도 유지됩니다.

## 에러 처리

- 도메인별 `@RestControllerAdvice`로 일관 응답:
  - 400: 유효성/잘못된 입력
  - 401: 비인증(토큰 문제)
  - 403: 권한 없음(작성자 아님 등)
  - 404: 리소스 없음

## Postman 빠른 시작

1) 회원가입/로그인
- `POST /api/v1/auth/signup` → 계정 생성
- `POST /api/v1/auth/login` → `accessToken` 수신

2) 프로젝트 생성(JSON)
- `POST /api/v1/projects/new`
- 헤더: `Authorization: Bearer {access_token}`, `Content-Type: application/json`
- 예시 바디:
```
{
  "projectTitle": "Team NEST",
  "projectDescription": "커뮤니티 앱 개발",
  "isRecruiting": true,
  "tags": ["SPRING", "REACT"],
  "partCounts": {"BACKEND": 2, "FRONTEND": 2},
  "creatorPart": "BACKEND"
}
```

3) 이미지 포함 생성(Multipart)
- `POST /api/v2/projects`
- 헤더: `Authorization: Bearer {access_token}`
- Body: `form-data`
  - 텍스트: `projectTitle`, `projectDescription`, `isRecruiting`, `creatorPart`, `tags`(여러 번 추가 가능), `partCounts[BACKEND]`와 같이 key에 대괄호 문법 사용 가능
  - 파일: `images` (복수 업로드 가능)

4) 조회
- `GET /api/v1/projects` (공개)
- `GET /api/v1/projects/{id}`

문제 해결
- 401 Unauthorized: 토큰 누락/만료/서명 오류 → 로그인 후 `Authorization: Bearer ...` 재전송
- 403 Forbidden: 인증은 되었으나 권한 부족(타인 리소스 수정 등) 또는 잘못된 경로 → 생성은 반드시 `/api/v1/projects/new` 또는 `/api/v2/projects` 사용

## 프로젝트 구조

- `src/main/java/com/virtukch/nest/**`: 도메인 패키지(auth, member, project, post, comment, tag, tech_stack, interest, notice, follow, chatting_room 등)
- `src/main/resources/application.yaml`: DB/JWT/메일/포트 설정
- `uploaded-images/`: 이미지 저장 디렉터리(런타임)
- `Dockerfile`, `docker-build.sh`: 컨테이너화 스크립트
- `System_Architecture.drawio`: 아키텍처 다이어그램 원본

## 보안 주의

- `application.yaml`의 메일 계정/비밀번호, JWT 시크릿 등 민감 정보는 운영 환경에서 환경변수 또는 외부 설정으로 분리하세요(예: Docker/CI 환경 변수, Spring Config Server, KMS 등).

## 라이선스

명시된 라이선스가 없습니다. 외부 배포/재배포 전 저장소 소유자에게 문의하세요.
