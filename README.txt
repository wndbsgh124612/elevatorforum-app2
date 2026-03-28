엘리베이터포럼 안정화 버전

변경사항
- 시작 시 restoreState 제거
- SplashScreen 제거
- WebView 시작 로직 단순화
- 파일 선택기 null 안정화
- FCM 토큰 전송 실패가 앱 실행을 막지 않도록 처리
- GitHub Actions 워크플로 포함: .github/workflows/android.yml

빌드 방법
1. 새 GitHub 저장소 생성
2. 이 압축파일을 풀어서 전체 업로드
3. Actions > Build Android APK > Run workflow
4. Artifacts 에서 APK 다운로드
