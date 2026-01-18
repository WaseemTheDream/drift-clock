# Technical Specification: Drift Clock

## Authentication
None required. Fully local app with no user accounts.

## Data Architecture

### Storage
- **Local only**: All profiles and settings stored on device
- **SharedPreferences** or **DataStore** for profile selection and settings
- No cloud sync, no backend

### Offline Support
- **Full offline mode**: App works entirely offline
- No network permissions required

## Time Manipulation Engine

### Core Concept
The Timer and Stopwatch use a modified time calculation:
```
displayed_time = real_elapsed_time * speed_multiplier
```

### Speed Profiles
| Profile | Multiplier | Effect |
|---------|------------|--------|
| Turbo | 1.25x | Time runs 25% faster |
| Grind | 0.8x | Time runs 20% slower |
| Stealth | 1.1x | Time runs 10% faster (subtle) |
| Trickster | 1.5x | Time runs 50% faster |
| Custom | User-defined | Fine-tuned speed |

### Implementation Notes
- Use `System.nanoTime()` or `SystemClock.elapsedRealtime()` for accuracy
- Apply multiplier to elapsed time calculations
- Ensure smooth UI updates regardless of speed

## Notifications
- **Local notifications only**: Timer completion alerts
- Standard Android notification channel
- Sound/vibration on timer completion

## Backend & APIs
- None required
- No third-party services
- No analytics (unless added later for monetization decision)

## Architecture Recommendations
- **Single Activity** with Jetpack Compose
- **Bottom Navigation** with 5 tabs
- **ViewModel** for timer/stopwatch state
- **Coroutines** for timing logic
- **DataStore** for preferences
