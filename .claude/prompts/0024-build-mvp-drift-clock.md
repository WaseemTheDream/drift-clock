# Build Drift Clock MVP

[auto-commit]

## Overview

This prompt implements the complete MVP for Drift Clock - a stealth clock app with time manipulation hidden in plain sight.

## Approved Scope

- 5-tab bottom navigation mimicking stock Android Clock
- Functional Timer with time manipulation (speed profiles)
- Functional Stopwatch with time manipulation
- 5 preset speed profiles + Custom option
- Full-screen profile selection on first launch
- Hidden settings accessed via Bedtime tab long-press
- Local notifications for timer completion
- Light/dark theme support (follows system)

## Implementation Tasks

### Phase 1: Project Foundation

#### 1.1 Rename Package
- [ ] Change `com.example.launchpad` to `com.example.driftclock`
- [ ] Update all file paths and imports
- [ ] Update build.gradle.kts namespace and applicationId
- [ ] Update AndroidManifest.xml package references

#### 1.2 Update Dependencies
Add to build.gradle.kts if not present:
- [ ] DataStore Preferences
- [ ] Lifecycle ViewModel Compose (already present)
- [ ] Navigation Compose (already present)

#### 1.3 Set Up Theme
- [ ] Update Color.kt with stock Clock blue (#4285F4)
- [ ] Configure dark/light theme variants
- [ ] Update Theme.kt for Material 3 stock Clock look

### Phase 2: Navigation & Structure

#### 2.1 Create Navigation Graph
- [ ] Define all routes (ProfileSelection, Alarm, Clock, Timer, Stopwatch, Bedtime, HiddenSettings)
- [ ] Set up NavHost in MainActivity
- [ ] Create navigation helper functions
- [ ] Implement conditional start destination (onboarding vs main)

#### 2.2 Bottom Navigation
- [ ] Create BottomNavBar component with 5 tabs
- [ ] Icons: alarm, schedule, timer, timelapse/stopwatch, bedtime
- [ ] Labels: Alarm, Clock, Timer, Stopwatch, Bedtime
- [ ] Timer as default selected tab

### Phase 3: Data Layer

#### 3.1 Data Models
- [ ] Profile data class (id, name, description, speedMultiplier, isCustom)
- [ ] TimerState data class
- [ ] StopwatchState data class
- [ ] LapTime data class
- [ ] UserPreferences data class

#### 3.2 Repositories
- [ ] ProfileRepository with preset profiles
- [ ] PreferencesRepository with DataStore

#### 3.3 TimeEngine
- [ ] Core time manipulation class
- [ ] Apply speed multiplier to elapsed time

### Phase 4: Profile Selection Screen

- [ ] Full-screen layout
- [ ] 5 profile cards (Turbo, Grind, Stealth, Trickster, Custom)
- [ ] Card shows name, description, speed multiplier
- [ ] Tap to select and navigate to main app
- [ ] Save selection to DataStore

### Phase 5: Timer Screen

- [ ] Time input UI (hours, minutes, seconds)
- [ ] Large countdown display
- [ ] Start/Pause/Reset buttons
- [ ] TimerViewModel with TimeEngine integration
- [ ] Timer runs at selected profile speed
- [ ] Notification on completion
- [ ] Sound/vibration alert

### Phase 6: Stopwatch Screen

- [ ] Large elapsed time display (HH:MM:SS.ss)
- [ ] Start/Stop/Reset buttons
- [ ] Lap button
- [ ] Scrollable lap list
- [ ] StopwatchViewModel with TimeEngine integration
- [ ] Time runs at selected profile speed

### Phase 7: Fake Tabs

#### 7.1 Alarm Screen
- [ ] Static fake alarm list
- [ ] Non-functional FAB
- [ ] Stock Clock appearance

#### 7.2 Clock Screen
- [ ] Display real current time (large)
- [ ] Analog or digital clock
- [ ] Static world clock section

#### 7.3 Bedtime Screen
- [ ] Static bedtime schedule UI
- [ ] Wind Down section
- [ ] Long-press on moon/header → navigate to Hidden Settings

### Phase 8: Hidden Settings

- [ ] Profile quick-switch
- [ ] Custom speed slider (0.5x - 2.0x)
- [ ] Numeric speed input for precision
- [ ] Current profile display
- [ ] Reset to defaults

### Phase 9: Notifications

- [ ] Create notification channel
- [ ] Timer completion notification
- [ ] Sound/vibration support

### Phase 10: Polish

- [ ] Smooth animations
- [ ] Edge case handling
- [ ] Test all flows
- [ ] Verify on emulator

## Technical Specifications

- Architecture: MVVM
- DI: Manual
- Persistence: DataStore
- Time source: SystemClock.elapsedRealtime()
- Time formula: displayed_time = real_elapsed_time * speed_multiplier

## Speed Profiles

| Profile | Multiplier | Description |
|---------|------------|-------------|
| Turbo | 1.25x | Rest periods fly by |
| Grind | 0.8x | Make every second burn |
| Stealth | 1.1x | Subtle, fool yourself |
| Trickster | 1.5x | Party bets and tricks |
| Custom | 0.5-2.0x | User-defined |

## Commit Strategy

- [0002] feat: Set up project foundation for Drift Clock
- [0003] feat: Implement navigation structure and bottom nav
- [0004] feat: Add data layer with profiles and preferences
- [0005] feat: Implement profile selection screen
- [0006] feat: Add functional Timer with time manipulation
- [0007] feat: Add functional Stopwatch with laps
- [0008] feat: Implement fake tabs (Alarm, Clock, Bedtime)
- [0009] feat: Add hidden settings with custom speed
- [0010] feat: Add timer completion notifications
- [0011] feat: Polish and finalize Drift Clock MVP
