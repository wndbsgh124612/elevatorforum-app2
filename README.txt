ElevatorForum Android project (session+safearea fixed)

Changes:
- MainActivity updated to handle safe area (status/nav bars)
- FCM token save uses WebView fetch(credentials:'include') with session cookies
- Token URL: http://sellmatch.co.kr/rb/rb.lib/ajax.token_update.php
- Existing WebView features kept: file chooser, download, external intents, swipe refresh, back navigation

Build:
- Upload to GitHub and run Actions workflow in .github/workflows/android.yml
