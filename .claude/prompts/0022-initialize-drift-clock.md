# Initialize Drift Clock

[auto-commit]

## Overview
This prompt initializes the Launchpad project as Drift Clock - a stealth clock app with time manipulation.

## App Summary
A clock app that looks stock but isn't. Timers & Stopwatch run at adjustable speeds—faster or slower than real time. Perfect for training intervals, workout mind games, party tricks, or testing your perception.

## Task

Transform this Launchpad template into Drift Clock by:

### 1. Rename Package
- Change package from `com.example.launchpad` to `com.example.driftclock`
- Update all file locations and imports
- Update AndroidManifest.xml
- Update build.gradle.kts (applicationId)

### 2. Update App Identity
- Update app_name in strings.xml to "Clock" (stealth name - looks like stock)
- Update application label in AndroidManifest
- Consider using stock Clock icon or similar

### 3. Set Up Navigation
- Implement Bottom Navigation with 5 tabs:
  1. **Alarm** - Decorative/fake tab
  2. **Clock** - Decorative/fake tab (shows real time)
  3. **Timer** - Functional with speed manipulation
  4. **Stopwatch** - Functional with speed manipulation
  5. **Bedtime** - Decorative + hidden settings access
- Use Material Design 3 styling
- Match stock Android Clock appearance

### 4. Configure Theme
- Use stock Android Clock blue accent color
- Configure Material 3 theme in Theme.kt
- Support light and dark mode (follow system)
- Large display numbers for timer/stopwatch

### 5. Create Data Layer Foundation
- Set up DataStore for preferences
- Create Profile data class:
  ```kotlin
  data class Profile(
      val id: String,
      val name: String,
      val description: String,
      val speedMultiplier: Float
  )
  ```
- Preset profiles:
  - Turbo (1.25x)
  - Grind (0.8x)
  - Stealth (1.1x)
  - Trickster (1.5x)
  - Custom (user-defined)
- Create ProfileRepository

### 6. MVP Feature Stubs
Create placeholder implementations for:
- [ ] Profile selection screen (full-screen, first launch)
- [ ] Timer screen with basic UI
- [ ] Stopwatch screen with basic UI
- [ ] Fake Alarm tab UI
- [ ] Fake Clock tab UI (showing real time)
- [ ] Fake Bedtime tab UI with hidden settings trigger
- [ ] Hidden settings screen

### 7. Time Manipulation Engine
- Create TimeEngine class that applies speed multiplier
- Core calculation: `displayed_time = real_elapsed_time * speed_multiplier`
- Use accurate time sources (SystemClock.elapsedRealtime())

## Technical Notes
- Authentication: None
- Offline: Full offline support (no network needed)
- Storage: Local only (DataStore)
- Notifications: Local notifications for timer completion

## Commit
Commit: `[0022] feat: Initialize Drift Clock with core structure`
