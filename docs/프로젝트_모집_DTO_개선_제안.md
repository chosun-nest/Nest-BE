# 프로젝트 모집 DTO/엔드포인트 개선 제안

목표: 프론트엔드가 단순한 JSON/Multipart로 손쉽게 생성/수정 요청을 보낼 수 있도록 입력 스키마와 엔드포인트를 단순화하고, 하위호환을 유지합니다.

## 핵심 제안

- 슬롯(Map) → 리스트(배열) 구조로 단순화
- Multipart에서는 `payload`(JSON) + `images`(파일) 구조 채택
- 부분 수정(PATCH) 전용 라우트를 세분화해 복잡한 통합 DTO 의존도 낮춤
- 하위호환: 기존 `partCounts(Map<Part, Integer>)`도 계속 수용, 서버에서 통합 처리

## 1) 신규 공통 DTO (JSON 페이로드)

- ProjectSlotsDto
```
{
  "part": "BACKEND",   // enum: BACKEND|FRONTEND|PM|DESIGN|AI|ETC (대소문자 무시)
  "count": 2             // 정원(빈 슬롯 포함)
}
```

- ProjectUpsertRequest (v3 제안, v1에도 적용 가능)
```
{
  "projectTitle": "NEST 팀원 모집",
  "projectDescription": "커뮤니티 앱 백엔드 개발",
  "isRecruiting": true,
  "tags": ["UNCATEGORIZED"],
  "slots": [
    {"part": "BACKEND", "count": 2},
    {"part": "FRONTEND", "count": 2},
    {"part": "PM", "count": 1}
  ],
  "creatorPart": "BACKEND"
}
```

설명
- `slots`: 필수(생성 시). 수정 시 null=미수정, []=전체 제거(금지 권고)처럼 해석 가능하나, 안정성을 위해 전용 엔드포인트를 권장(아래 3장).
- `creatorPart`: 미제공 시 서버가 `slots`의 첫 파트를 자동 배정하도록 기본값 처리 가능.
- 하위호환: 기존 `partCounts`도 함께 지원. 서버에서 `slots` 우선 → 없으면 `partCounts` 사용.

## 2) Multipart(v2) 개선

- 현재: `@ModelAttribute ProjectWithImagesRequestDto` + `partCounts[KEY]` 형태 → Postman/UI에서 번거로움
- 개선: `@RequestPart("payload") ProjectUpsertRequest` + `@RequestPart(value="images", required=false) List<MultipartFile>`

form-data 예시
- key: `payload` (type: Text)
```
{
  "projectTitle": "NEST 팀원 모집(이미지)",
  "projectDescription": "이미지와 함께",
  "isRecruiting": true,
  "tags": ["UNCATEGORIZED"],
  "slots": [{"part": "BACKEND", "count": 2}, {"part": "FRONTEND", "count": 2}],
  "creatorPart": "BACKEND"
}
```
- key: `images` (type: File, multiple)

하위호환
- 기존 `@ModelAttribute` 경로를 유지하고, 신규 `@RequestPart payload` 경로를 추가(v2.1 또는 v3 경로)하여 점진 전환.

## 3) 부분 수정 라우트 세분화 (권장)

복잡한 통합 PATCH 대신 작은 단위로 나눠 프론트 난이도/실수 감소.

- 모집 여부 토글
  - `PATCH /api/v1/projects/{id}/recruiting`
  - Body: `{ "isRecruiting": true }`

- 태그 교체
  - `PATCH /api/v1/projects/{id}/tags`
  - Body: `{ "tags": ["UNCATEGORIZED", "SPRING"] }`

- 슬롯(정원) 재설정
  - `PATCH /api/v1/projects/{id}/slots`
  - Body: `{ "slots": [{"part":"BACKEND","count":3}, ...] }`
  - 서버 로직: 현재 채워진 인원 수 미만으로는 축소 불가(현행 규칙 유지), 빈 슬롯만 삭제/추가

- 작성자 파트 변경
  - `PATCH /api/v1/projects/{id}/creator-part`
  - Body: `{ "creatorPart": "PM" }`

- 텍스트 필드 수정
  - `PATCH /api/v1/projects/{id}/text`
  - Body: `{ "projectTitle": "새 제목", "projectDescription": "새 설명" }`

## 4) 서버 수용 로직 (요약)

- DTO 수용 우선순위
  - `slots` 있으면 사용 → 없으면 `partCounts` 사용
- Multipart
  - `payload`(JSON) + `images`(파일 배열)
- PATCH 규약(기반 코드 반영됨)
  - `null` 값 → 미수정
  - `projectDescription == ""` → 삭제(null)
  - `tags == null` → 미수정, `tags == []` → 전부 제거

## 5) 마이그레이션/하위호환

- v1/v2 기존 엔드포인트 유지
- v2에 `payload` 지원 추가(v2.1) 또는 신규 v3 경로 제공
- FE는 점진적으로 `slots`/`payload` 방식으로 전환

## 6) 검증/오류 메시지 UX

- `slots` 비어있음 → 400 + "최소 1개 역할 필요"
- `creatorPart`가 `slots`에 없음 → 400 + "creatorPart는 slots에 포함되어야 합니다"
- 알 수 없는 태그 → 400 + 태그 목록 조회 링크 제공(`/api/v1/tags`)
- enum 대소문자 허용 및 친절한 메시지

## 7) 예시 스니펫 (Postman Raw JSON)

생성(v1)
```
POST /api/v1/projects/new
{
  "projectTitle": "NEST",
  "projectDescription": "플랫폼 백엔드",
  "isRecruiting": true,
  "tags": ["UNCATEGORIZED"],
  "slots": [
    {"part":"BACKEND","count":2},
    {"part":"FRONTEND","count":2}
  ],
  "creatorPart":"BACKEND"
}
```

수정(슬롯 재설정)
```
PATCH /api/v1/projects/{id}/slots
{
  "slots": [
    {"part":"BACKEND","count":3},
    {"part":"FRONTEND","count":1}
  ]
}
```

---
필요 시 위 제안을 기준으로 DTO/컨트롤러 실제 코드 패치까지 진행하겠습니다. 하위호환을 유지하며 신규 필드/엔드포인트를 추가하는 방향이 안전합니다.
