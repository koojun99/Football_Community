# Football Community - React 프론트엔드 개발 프롬프트

## 프로젝트 개요

Football Community는 축구 경기 정보, 위키, 댓글, 실시간 경기 이벤트 등을 제공하는 웹 애플리케이션입니다.
백엔드는 Spring Boot 기반 REST API를 제공하며, JWT 기반 인증을 사용합니다.

## 기술 스택 요구사항

- **React 18+** (TypeScript 권장)
- **React Router** (라우팅)
- **Axios** (HTTP 클라이언트)
- **React Query / SWR** (서버 상태 관리, 선택사항)
- **Tailwind CSS** 또는 **Styled Components** (스타일링)
- **Zustand** 또는 **Context API** (클라이언트 상태 관리)

## 디자인 가이드라인

- **주요 색상**: 흰색(#FFFFFF) 위주, 파랑(#2563EB 또는 #3B82F6) 포인트 색
- **디자인 스타일**: 깔끔하고 모던한 UI/UX
- **반응형 디자인**: 모바일, 태블릿, 데스크톱 지원
- **접근성**: WCAG 2.1 AA 수준 준수

## API 기본 정보

### Base URL

```
http://localhost:8080 (개발 환경)
```

### 공통 응답 형식

모든 API 응답은 다음 형식을 따릅니다:

```typescript
interface CommonResponse<T> {
  isSuccess: boolean;
  code: string;
  message: string;
  data: T;
}
```

### 인증

- **JWT 토큰** 기반 인증
- **Access Token**: 요청 헤더에 `Authorization: Bearer {accessToken}` 형식으로 전송
- **Refresh Token**: Access Token 만료 시 갱신에 사용
- 토큰은 `localStorage` 또는 `httpOnly cookie`에 저장

---

## API 엔드포인트 상세

### 1. 인증 (Auth)

#### 1.1 이메일 중복 확인

```
GET /auth/email-verification?email={email}
```

**인증**: 불필요

#### 1.2 회원가입

```
POST /auth/signup
```

**Request Body**:

```typescript
{
  name: string;        // 필수
  email: string;       // 필수
  password: string;   // 필수
  username: string;   // 필수
  bio?: string;       // 선택
  phoneNumber?: string; // 선택
}
```

**Response**: `CommonResponse<{ memberId: number }>`
**인증**: 불필요

#### 1.3 로그인

```
POST /auth/login
```

**Request Body**:

```typescript
{
  email: string; // 필수
  password: string; // 필수
}
```

**Response**: `CommonResponse<{ memberId: number, accessToken: string, refreshToken: string }>`
**인증**: 불필요

#### 1.4 비밀번호 변경

```
PATCH /auth/password
```

**Request Body**:

```typescript
{
  currentPassword: string; // 필수
  newPassword: string; // 필수
}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

---

### 2. 회원 관리 (Member)

#### 2.1 내 프로필 조회

```
GET /members/my-profile
```

**Response**: `CommonResponse<{ memberId: number, name: string, username: string, email: string, bio: string, phoneNumber: string }>`
**인증**: 필요

#### 2.2 회원 프로필 조회

```
GET /members/{memberId}
```

**Response**: `CommonResponse<{ memberId: number, username: string, bio: string }>`
**인증**: 불필요

#### 2.3 회원 정보 수정

```
PUT /members
```

**Request Body**:

```typescript
{
  name?: string;
  username?: string;
  bio?: string;
  phoneNumber?: string;
}
```

**Response**: `CommonResponse<{ memberId: number }>`
**인증**: 필요

#### 2.4 회원 탈퇴

```
DELETE /members
```

**Response**: `CommonResponse<{ memberId: number }>`
**인증**: 필요

#### 2.5 회원 블라인드

```
POST /members/{memberId}/blind
```

**Response**: `CommonResponse<null>`
**인증**: 필요

#### 2.6 회원 블라인드 해제

```
PATCH /members/{memberId}/blind
```

**Response**: `CommonResponse<null>`
**인증**: 필요

---

### 3. 경기 정보 (Fixture)

#### 3.1 오늘 기준 예정된 경기 조회

```
GET /fixtures/upcoming?leagueId={leagueId}&timezone={timezone}
```

**Query Parameters**:

- `leagueId` (optional): 리그 ID (기본값: 39 - 프리미어리그)
- `timezone` (optional): 타임존 (기본값: "Asia/Seoul")

**Response**: `CommonResponse<{ fixtures: Fixture[] }>`

```typescript
interface Fixture {
  fixtureId: number;
  league: string;
  homeTeam: string;
  awayTeam: string;
  scores: {
    homeTeamScore: number | null;
    awayTeamScore: number | null;
  };
  fixtureDate: string; // ISO 8601 형식
  fixtureStatus: string; // "NS", "H1", "HT", "H2", "FT", "AET", "PEN", "CANC", "ABD" 등
  events?: FixtureEvent[]; // 경기 이벤트 (종료된 경기의 경우에만 포함)
}

interface FixtureEvent {
  type: string; // 이벤트 타입 (예: "Goal", "Card", "subst" 등)
  time: number | null; // 이벤트 발생 시간 (분)
  detail: string | null; // 이벤트 상세 (예: "Normal Goal", "Yellow Card" 등)
  comments: string | null; // 이벤트 코멘트
  teamId: number | null; // 팀 ID
  teamName: string | null; // 팀 이름
  playerId: number | null; // 선수 ID
  playerName: string | null; // 선수 이름
  assistId: number | null; // 어시스트 선수 ID
  assistName: string | null; // 어시스트 선수 이름
}

// 경기 상태 확인 유틸리티 함수 예시
function isFinishedFixture(fixtureStatus: string): boolean {
  const finishedStatuses = ["FT", "AET", "PEN", "CANC", "ABD", "AWD", "WO"];
  return finishedStatuses.includes(fixtureStatus);
}

function isInProgressFixture(fixtureStatus: string): boolean {
  const inProgressStatuses = [
    "H1",
    "H2",
    "ET",
    "BT",
    "P",
    "SUSP",
    "INT",
    "LIVE",
  ];
  return inProgressStatuses.includes(fixtureStatus);
}
```

**인증**: 불필요

**참고**:

- `fixtureStatus`는 문자열이며, 메서드가 아닙니다. 경기가 종료되었는지 확인하려면 위의 유틸리티 함수를 사용하세요.
- 종료된 경기(`FT`, `AET`, `PEN` 등)의 경우 `events` 필드가 포함됩니다.

#### 3.2 경기 선발 라인업 조회

```
GET /fixtures/{fixtureId}/lineups
```

**Response**: `CommonResponse<FixtureLineupResponse>`

```typescript
interface FixtureLineupResponse {
  lineups: TeamLineup[];
}

interface TeamLineup {
  teamId: number | null;
  teamName: string | null;
  formation: string | null;
  starters: LineupPlayer[];
}

interface LineupPlayer {
  playerId: number | null;
  playerName: string | null;
  number: number | null;
  position: string | null; // 예: "G", "D", "M", "F"
  grid: string | null;     // 포메이션 내 좌표 (예: "1:1")
}
```

**인증**: 불필요

**참고**:

- 과거 경기, 진행 중인 경기 모두 동일하게 조회 가능합니다.
- **경기 시작 전이면 `lineups` 배열이 빈 배열 `[]`로 반환**됩니다.
- 홈/원정 두 팀의 라인업이 `lineups` 배열에 각각 하나의 `TeamLineup`으로 들어옵니다.

#### 3.3 경기 선수 통계 조회

```
GET /fixtures/{fixtureId}/players
```

**Response**: `CommonResponse<FixturePlayerStatsResponse>`

```typescript
interface FixturePlayerStatsResponse {
  fixtureId: number | null;
  players: PlayerStats[];
}

interface PlayerStats {
  playerId: number | null;
  playerName: string | null;
  teamId: number | null;
  teamName: string | null;
  position: string | null;
  minutes: number | null;

  goals: number | null;
  assists: number | null;
  shotsTotal: number | null;
  shotsOnTarget: number | null;
  passesTotal: number | null;
  passesKey: number | null;
  passesAccuracy: string | null; // 예: "85"
  tacklesTotal: number | null;
  interceptions: number | null;
  foulsCommitted: number | null;
  foulsDrawn: number | null;
  yellowCards: number | null;
  redCards: number | null;
}
```

**인증**: 불필요

**참고**:

- 과거 경기, 진행 중인 경기 모두 동일하게 조회 가능합니다.
- 경기 시작 전이거나 통계 데이터가 아직 없으면 `players`는 빈 배열 `[]`일 수 있습니다.
- 이 API는 **실시간 평점이 아니라 원시 스탯 데이터**만 제공합니다. 필요하다면 프론트에서 이 스탯을 기반으로 별도 평점/지표를 계산할 수 있습니다.

#### 3.4 오늘 기준 과거 경기 기록 조회

```
GET /fixtures/past?leagueId={leagueId}&timezone={timezone}&last={last}
```

**Query Parameters**:

- `leagueId` (optional): 리그 ID (기본값: 39)
- `timezone` (optional): 타임존 (기본값: "Asia/Seoul")
- `last` (optional): 조회할 과거 경기 수 (기본값: 10)

**Response**: `CommonResponse<{ fixtures: Fixture[] }>`
**인증**: 불필요

**참고**:

- **Follow 여부와 관계없이** 모든 과거 경기 기록을 조회할 수 있습니다.
- 인증이 필요하지 않으므로 비회원도 조회 가능합니다.

#### 3.3 Follow한 경기 정보 조회

```
GET /fixtures/followed
```

**Response**: `CommonResponse<{ fixtures: Fixture[] }>`
**인증**: 필요

#### 3.4 경기 상세 조회

```
GET /fixtures/{fixtureId}
```

**Response**: `CommonResponse<Fixture>`

**참고**:

- **Follow 여부와 관계없이** 모든 경기의 상세 정보를 조회할 수 있습니다.
- 인증이 필요하지 않으므로 비회원도 조회 가능합니다.
- **종료된 경기(`fixtureStatus`가 "FT", "AET", "PEN" 등)의 경우 항상 `events` 필드에 경기 이벤트 기록이 포함됩니다.**
  - 백엔드에서 종료된 경기는 캐시를 사용하지 않으므로 항상 최신 이벤트 데이터를 조회합니다.
  - 진행 중인 경기는 캐시를 사용하여 성능을 최적화합니다.
- `fixtureStatus`는 문자열이므로 직접 비교하거나 유틸리티 함수를 사용해야 합니다.

**인증**: 불필요

#### 3.5 팀의 경기 일정 조회

```
GET /fixtures/team/{teamId}
```

**Response**: `CommonResponse<{ fixtures: Fixture[] }>`
**인증**: 불필요

#### 3.6 리그의 경기 일정 조회

```
GET /fixtures/league/{leagueId}
```

**Response**: `CommonResponse<{ fixtures: Fixture[] }>`
**인증**: 불필요

---

### 4. Follow 기능

#### 4.1 경기 Follow 추가

```
POST /follows
```

**Request Body**:

```typescript
{
  fixtureId: number;
}
```

**Response**: `CommonResponse<{ id: number, fixtureId: number }>`
**인증**: 필요

#### 4.2 Follow 삭제

```
DELETE /follows/{followId}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

#### 4.3 Follow 목록 조회

```
GET /follows
```

**Response**: `CommonResponse<{ follows: { id: number, fixtureId: number }[] }>`
**인증**: 필요

---

### 5. 실시간 경기 이벤트 (SSE)

#### 5.1 실시간 경기 이벤트 구독

```
GET /fixtures/events/{fixtureId}/subscribe
```

**Content-Type**: `text/event-stream`

**이벤트 형식**:

- `connected`: 연결 성공
- `fixture-event`: 경기 이벤트 발생
- `error`: 오류 발생

**이벤트 데이터 예시**:

```typescript
{
  fixtureId: number;
  eventType: string; // "goal", "card", "substitution" 등
  eventData: string; // JSON 문자열
  timestamp: number;
}
```

**주의사항**:

- **Follow된 경기만 구독 가능** (SSE는 실시간 이벤트이므로 Follow된 경기에만 제공)
- **진행 중인 경기에만 SSE 연결** (종료된 경기는 `events` 필드 사용)
- **과거 경기(종료된 경기)는 SSE 연결하지 않음** - `GET /fixtures/{fixtureId}`로 조회하면 `events` 필드에 모든 이벤트가 포함됨
- SSE 연결은 클라이언트가 직접 관리해야 함
- 연결 해제 시 자동으로 정리됨
- 경기 상태가 종료로 변경되면 SSE 연결 해제

**인증**: 불필요 (하지만 Follow 여부 확인 필요)

**참고**:

- 과거 경기(종료된 경기)의 경우 `GET /fixtures/{fixtureId}` API로 조회하면 `events` 필드에 모든 이벤트 기록이 포함되므로, SSE 연결 없이도 경기 기록을 확인할 수 있습니다.
- 경기 상세 조회(`GET /fixtures/{fixtureId}`)는 Follow 여부와 관계없이 모든 경기를 조회할 수 있습니다.

---

### 6. 위키 (Wiki)

#### 6.1 위키 목록 조회

```
GET /wikis
```

**Response**: `CommonResponse<{ wikis: Wiki[] }>`
**인증**: 불필요

#### 6.2 위키 조회

```
GET /wikis/{wikiId}
```

**Response**: `CommonResponse<Wiki>`
**인증**: 불필요

#### 6.3 카테고리 생성

```
POST /wikis/{wikiId}/categories
```

**Request Body**:

```typescript
{
  name: string; // 카테고리 이름 (필수)
  description: string; // 카테고리 내용 (필수)
}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

**참고**: 카테고리는 자동으로 목차 순서(orderIndex)가 할당됩니다. 나무위키의 목차처럼 사용자가 자유롭게 생성할 수 있는 인덱스의 개념입니다.

#### 6.4 카테고리 수정

```
PUT /wikis/{wikiId}/categories/{wikiCategoryId}
```

**Request Body**:

```typescript
{
  name?: string;        // 카테고리 이름 (선택)
  description?: string; // 카테고리 내용 (선택)
}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

**참고**: 이름과 내용 중 하나 또는 둘 다 수정할 수 있습니다.

#### 6.5 카테고리 삭제

```
DELETE /wikis/{wikiId}/categories/{wikiCategoryId}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

**Wiki 타입 정의**:

```typescript
interface Wiki {
  id: number;
  title: string;
  categories: Category[];
}

interface Category {
  id: number;
  name: string;
  description: string;
  orderIndex: number; // 목차 순서 (나무위키의 목차처럼)
}
```

---

### 7. 댓글 (Comment)

#### 7.1 댓글 작성

```
POST /wikis/{wikiId}/comments
```

**Request Body**:

```typescript
{
  body: string;
}
```

**Response**: `CommonResponse<{ commentId: number }>`
**인증**: 필요

#### 7.2 댓글 조회

```
GET /wikis/{wikiId}/comments
```

**Response**: `CommonResponse<{ comments: Comment[] }>`

```typescript
interface Comment {
  commentId: number;
  body: string;
  writerId: number;
  writerName: string;
}
```

**인증**: 불필요

#### 7.3 댓글 수정

```
PUT /wikis/{wikiId}/comments/{commentId}
```

**Request Body**:

```typescript
{
  body: string;
}
```

**Response**: `CommonResponse<{ commentId: number }>`
**인증**: 필요

#### 7.4 댓글 삭제

```
DELETE /wikis/{wikiId}/comments/{commentId}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

---

### 8. 즐겨찾기 (Favorite)

#### 8.1 즐겨찾기 추가

```
POST /favorites
```

**Request Body**:

```typescript
{
  targetId: number;
  favoriteType: "TEAM" | "LEAGUE"; // enum
}
```

**Response**: `CommonResponse<{ id: number, favoriteType: string, targetId: number }>`
**인증**: 필요

#### 8.2 즐겨찾기 삭제

```
DELETE /favorites/{favoriteId}
```

**Response**: `CommonResponse<null>`
**인증**: 필요

#### 8.3 즐겨찾기 목록 조회

```
GET /favorites
```

**Response**: `CommonResponse<{ favorites: Favorite[] }>`

```typescript
interface Favorite {
  id: number;
  favoriteType: "TEAM" | "LEAGUE";
  targetId: number;
}
```

**인증**: 필요

---

### 9. 헬스체크

#### 9.1 헬스체크

```
GET /health
```

**Response**: `CommonResponse<{ status: string, database: { status: string, message: string } }>`
**인증**: 불필요

---

## 주요 기능 구현 가이드

### 1. 인증 시스템

- **로그인/회원가입 페이지**: 깔끔한 폼 디자인, 파랑 포인트 색 사용
- **토큰 관리**: Axios 인터셉터로 자동 토큰 추가 및 갱신
- **로그인 상태 관리**: Context API 또는 Zustand로 전역 상태 관리
- **보호된 라우트**: 인증이 필요한 페이지는 Protected Route로 감싸기

### 2. 경기 정보 페이지

- **예정된 경기 목록**: 카드 형태로 표시, 경기 날짜/시간, 팀명, 상태 표시
- **과거 경기 기록**: 결과 점수 강조 표시
- **경기 상세 페이지**:
  - 경기 정보 (팀, 점수, 날짜, 상태)
  - Follow 버튼
  - **진행 중인 경기**: 실시간 이벤트 스트림 (SSE) 연결
    - `isInProgressFixture(fixtureStatus)`로 확인 후 SSE 연결
    - 실시간 이벤트를 받아서 표시
  - **종료된 경기**: 경기 이벤트 기록 (`events` 필드) 표시
    - SSE 연결하지 않음 (이미 모든 이벤트가 `events` 필드에 포함됨)
    - 골, 카드, 교체 등의 이벤트를 시간순으로 표시
    - 이벤트 타입별 아이콘/색상 구분
- **Follow한 경기**: 별도 탭 또는 페이지로 구성

### 3. 실시간 이벤트 (SSE)

- **EventSource API** 사용
- **중요**: 진행 중인 경기(`H1`, `H2`, `ET`, `LIVE` 등)에만 SSE 연결
- **종료된 경기(`FT`, `AET`, `PEN` 등)는 SSE 연결하지 않음** - 이미 `events` 필드에 모든 이벤트가 포함됨
- 경기 상세 페이지에서 경기 상태를 확인한 후 진행 중인 경우에만 SSE 연결
- 이벤트 타입별 아이콘/색상 구분 (골, 카드, 교체 등)
- 실시간 알림 토스트 표시

### 4. 위키 페이지

- **위키 목록 페이지**: 모든 위키를 카드 형태로 표시, 제목과 카테고리 미리보기
- **위키 상세 페이지**:
  - 위키 제목과 카테고리(목차) 표시
  - 카테고리는 orderIndex 순서대로 정렬되어 표시 (나무위키의 목차처럼)
  - 카테고리별 내용(description) 표시
  - 카테고리 생성/수정/삭제 기능 (인증 필요)
- **댓글 섹션**:
  - 댓글 목록
  - 댓글 작성 폼
  - 댓글 수정/삭제 기능 (작성자만)
- 깔끔한 레이아웃, 가독성 중시

### 5. 프로필 페이지

- 내 프로필 / 다른 회원 프로필 구분
- 프로필 정보 표시 및 수정
- Follow 목록, Favorite 목록 표시

### 6. 디자인 컴포넌트 예시

#### 색상 팔레트

```css
--primary-blue: #2563eb; /* 또는 #3B82F6 */
--primary-blue-hover: #1d4ed8;
--primary-blue-light: #dbeafe;
--background-white: #ffffff;
--background-gray: #f9fafb;
--text-primary: #111827;
--text-secondary: #6b7280;
--border-color: #e5e7eb;
```

#### 버튼 스타일

- **Primary Button**: 파랑 배경, 흰색 텍스트
- **Secondary Button**: 흰색 배경, 파랑 테두리, 파랑 텍스트
- **Ghost Button**: 투명 배경, 파랑 텍스트

#### 카드 스타일

- 흰색 배경
- 그림자 효과 (subtle)
- 파랑 포인트 요소 (버튼, 링크 등)

---

## 구현 우선순위

### Phase 1: 핵심 기능

1. 인증 시스템 (로그인/회원가입)
2. 경기 정보 조회 (예정/과거/상세)
3. Follow 기능
4. 기본 레이아웃 및 네비게이션

### Phase 2: 실시간 기능

1. SSE 실시간 이벤트 스트림 (진행 중인 경기에만)
2. 실시간 알림 시스템
3. 경기 상태별 SSE 연결/해제 로직

### Phase 3: 커뮤니티 기능

1. 위키 페이지
2. 댓글 시스템
3. 프로필 페이지

### Phase 4: 추가 기능

1. Favorite 기능
2. 검색 기능
3. 필터링 및 정렬

---

## 주의사항

1. **CORS**: 백엔드에서 CORS 설정이 되어 있지만, 개발 환경에서 문제가 발생할 수 있으니 확인 필요
2. **에러 처리**: 모든 API 호출에 대한 에러 핸들링 구현
3. **로딩 상태**: API 호출 중 로딩 인디케이터 표시
4. **토큰 갱신**: Access Token 만료 시 Refresh Token으로 자동 갱신 로직 구현
5. **SSE 연결 관리**:
   - 컴포넌트 언마운트 시 SSE 연결 해제
   - 종료된 경기(`isFinishedFixture(fixtureStatus) === true`)에는 SSE 연결하지 않음
   - 진행 중인 경기에만 SSE 연결 (`isInProgressFixture(fixtureStatus) === true`)
6. **날짜/시간 포맷**: 백엔드에서 ISO 8601 형식으로 전달되므로, 클라이언트에서 적절히 포맷팅
7. **경기 상태 표시**: NS, H1, HT, H2, FT 등 상태를 사용자 친화적으로 표시

---

## 추가 참고사항

- 백엔드 Swagger 문서: `http://localhost:8080/swagger-ui.html`
- 모든 API는 RESTful 원칙을 따름
- 인증이 필요한 API는 `Authorization` 헤더에 Bearer 토큰 포함
- 에러 응답도 `CommonResponse` 형식을 따름 (isSuccess: false)

---

## 예시 컴포넌트 구조

```
src/
├── components/
│   ├── common/
│   │   ├── Button.tsx
│   │   ├── Card.tsx
│   │   ├── Input.tsx
│   │   └── Loading.tsx
│   ├── auth/
│   │   ├── LoginForm.tsx
│   │   └── SignupForm.tsx
│   ├── fixture/
│   │   ├── FixtureCard.tsx
│   │   ├── FixtureDetail.tsx
│   │   └── FollowButton.tsx
│   └── wiki/
│       ├── WikiContent.tsx
│       └── CommentSection.tsx
├── pages/
│   ├── Home.tsx
│   ├── Login.tsx
│   ├── Signup.tsx
│   ├── Fixtures.tsx
│   ├── FixtureDetail.tsx
│   ├── Wiki.tsx
│   └── Profile.tsx
├── services/
│   ├── api.ts (Axios 인스턴스)
│   ├── auth.ts
│   ├── fixture.ts
│   └── wiki.ts
├── hooks/
│   ├── useAuth.ts
│   ├── useFixture.ts
│   └── useSSE.ts
├── store/
│   └── authStore.ts (Zustand 또는 Context)
└── utils/
    ├── date.ts
    └── constants.ts
```

---

이 프롬프트를 기반으로 React 프론트엔드 애플리케이션을 개발해주세요. 디자인은 깔끔하고 모던하며, 흰색 위주에 파랑 포인트 색을 사용하여 일관성 있는 UI를 구현해주세요.
