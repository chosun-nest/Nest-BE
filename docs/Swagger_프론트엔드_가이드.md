# Swagger UI 사용 가이드 (Frontend 전용)

## 접속
- Swagger UI: `http://localhost:6030/swagger-ui.html`
- OpenAPI JSON: `http://localhost:6030/v3/api-docs`

## 공통 규칙
- 인증 필요 API는 `Authorize` 버튼으로 Bearer 토큰을 입력한 뒤 테스트하세요.
- Content-Type 주의:
  - JSON: `application/json`
  - Multipart: `multipart/form-data`
  - Form-urlencoded: `application/x-www-form-urlencoded`

## 프로젝트 모집 API 요약

- 생성(v1 JSON): `POST /api/v1/projects/new`
- 생성(v2 multipart): `POST /api/v2/projects`
- 생성/수정(v3 payload+images): `POST|PATCH /api/v3/projects[/{id}]`
  - v3에서는 form-data에 `payload`(JSON) + `images`(File[])로 전송
- 수정(v1 JSON): `PATCH /api/v1/projects/{id}`
- 수정(v2 multipart): `PATCH /api/v2/projects/{id}`
- 목록: `GET /api/v1/projects`
- 상세: `GET /api/v1/projects/{id}`
- 검색: `GET /api/v1/projects/search`

### v3 payload 스키마
- 타입: `ProjectUpsertRequest`
- 필드:
  - `projectTitle`: string (필수)
  - `projectDescription`: string (빈 문자열이면 삭제)
  - `isRecruiting`: boolean (null이면 미수정)
  - `tags`: string[] (null이면 미수정, []이면 전체 제거)
  - `slots`: `{ part: enum(BACKEND|FRONTEND|PM|DESIGN|AI|ETC), count: number }[]`
  - `creatorPart`: enum (슬롯에 포함되어야 함)
  - `partCounts`: map (하위호환)

### PATCH 규약(응답 일관성)
- null: 미수정
- description == "": 삭제(null)
- tags null: 미수정, `[]`: 전체 제거
- v2/v3 이미지: 미포함 → 유지, 포함 → 전체 교체

## 프로젝트 지원/관리 API 요약

- 지원: `POST /api/v1/projects/{projectId}/apply`
  - JSON 바디: `{ "part": "BACKEND" }`
  - 또는 form-urlencoded: `part=BACKEND`
- 지원자 목록(작성자): `GET /api/v1/projects/{projectId}/applications`
- 수락/거절(작성자): `POST .../accept`, `POST .../reject`
- 내 지원서 목록: `GET /api/v1/projects/applications/me`
- 지원 취소(지원자): `POST /api/v1/projects/{projectId}/applications/{applicationId}/cancel`

### 지원 제약
- 모집 종료(isRecruiting=false) → 불가
- 이미 멤버 → 불가
- 파트 정원 가득 참 → 불가
- 중복 지원 방지(WAITING/ACCEPTED 존재 시)

## 예시 요청(프론트 참고)

### v3 생성(form-data)
- key: `payload` (Text)
```
{
  "projectTitle": "NEST 팀원 모집(v3)",
  "projectDescription": "payload + images",
  "isRecruiting": true,
  "tags": ["UNCATEGORIZED"],
  "slots": [
    {"part": "BACKEND", "count": 2},
    {"part": "FRONTEND", "count": 2}
  ],
  "creatorPart": "BACKEND"
}
```
- key: `images` (File), 여러 개 첨부 가능

### 지원(JSON)
```
POST /api/v1/projects/{projectId}/apply
{
  "part": "BACKEND"
}
```

### 응답 타입 참고
- `ProjectResponseDto`: `{ projectId: number, message: string }`
- `ProjectApplicationResponseDto`: `{ applicationId, memberId, memberName, part, status, appliedAt }`

## 팁
- Swagger의 “Try it out”로 바로 테스트 가능
- enum 값은 대소문자 구분 없이 사용 가능하도록 백엔드에서 처리 중이나, UI에서는 상수 대문자 사용을 권장합니다.
- 파일 업로드는 브라우저 환경에서 `FormData` 사용 권장(`append('payload', JSON.stringify(obj))`, `append('images', file)`).
