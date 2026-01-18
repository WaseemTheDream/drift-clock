# Blueprint Session: Drift Clock

> Session documentation for the /blueprint command execution

## Session Info
- **Date**: 2026-01-17
- **App Name**: Drift Clock

## Product Documentation Analyzed

Files found in product-docs/:
- `README.md` - Template instructions only (no product details)

No existing product documentation was provided. All information gathered through interactive questionnaire.

## Interactive Questionnaire

### Product Vision

**Q: What is the name of your app?**
A: Drift Clock

**Q: Describe your app in one sentence**
A: A clock app that looks stock but isn't. Timers & Stopwatch run at adjustable speeds—faster or slower than real time. Perfect for training intervals, workout mind games, party tricks, or testing your perception.

**Q: What problem does it solve?**
A: Helps users manipulate their perception of time during workouts, study sessions, and for entertainment (jokes, tricks, magic). The stealth design means no one knows you're using a modified timer.

**Q: What makes it unique?**
A: Speed controls hidden in plain sight. Looks exactly like stock Android Clock app. 5 tabs (Alarm, Clock, Timer, Stopwatch, Bedtime) where only Timer and Stopwatch are functional. Hidden settings under Bedtime tab.

### Target Audience

**Q: Who are your primary users?**
A: A mix of fitness enthusiasts, students, pranksters, and magicians.

**Q: Device types?**
A: Phone only

**Q: Accessibility requirements?**
A: Standard (no special requirements)

### Core Features

**Q: MVP features (3-5 must-haves)?**
A:
1. Stock-looking 5-tab UI (Alarm, Clock, Timer, Stopwatch, Bedtime)
2. Functional Timer with adjustable speed
3. Functional Stopwatch with adjustable speed
4. Quick profile selection on first launch
5. Hidden settings under Bedtime tab

**Q: Future features?**
A: None planned (MVP-focused)

**Q: Out of scope?**
A: None specified

### Technical Requirements

**Q: Authentication?**
A: None (profiles stored locally)

**Q: Data storage?**
A: Local only (settings saved on device)

**Q: Offline support?**
A: Yes - full offline mode

**Q: Backend/APIs?**
A: None required

**Q: Notifications?**
A: Local notifications (timer completion alerts)

### Design & UX

**Q: Design style?**
A: Material Design 3 (matches modern Android Clock app)

**Q: Color scheme?**
A: Stock blue accent (classic Android Clock blue)

**Q: Navigation pattern?**
A: Bottom Navigation with 5 tabs

**Q: Profile selection UI?**
A: Full screen (dedicated onboarding screen)

**Q: Inspiration apps?**
A: Stock Android Clock app only

### Profiles Defined

| Profile | Speed | Description |
|---------|-------|-------------|
| Turbo | 1.25x | "Rest periods fly by. That 60-second break? Done in 48." |
| Grind | 0.8x | "Make every second burn. Your 30-second plank just became 36." |
| Stealth | 1.1x | "Subtle enough to fool yourself. Perfect for pushing through study sessions." |
| Trickster | 1.5x | "For party bets and bar tricks. 'Watch me hold my breath for 2 minutes.'" |
| Custom | User-defined | "Dial in your own time distortion. Fine-tune to the decimal." |

### Business Context

**Q: Monetization?**
A: Not decided yet

**Q: Timeline?**
A: Not specified

**Q: Compliance requirements?**
A: None

### Additional Context

**Q: Existing assets?**
A: None

**Q: Team & skills?**
A: Solo project, very experienced Android software engineer

**Q: Anything else?**
A: Looking to make this a useful app for now

## Generated Context Files

The following files were created in `.claude/context/`:

### product-vision.md
- App overview and one-liner description
- Problem statement and solution
- Unique value proposition
- Target audience breakdown

### technical-spec.md
- Authentication: None
- Storage: Local only (DataStore)
- Offline: Full support
- Time manipulation engine specification
- Speed profile definitions
- Architecture recommendations

### feature-roadmap.md
- Complete MVP feature checklist
- 5-tab navigation structure
- Profile system details
- Timer and Stopwatch functional requirements
- Hidden settings access

### design-guidelines.md
- Material Design 3 styling
- Stock Android Clock color scheme
- Navigation structure
- Component guidelines for each screen
- Stealth design philosophy

## Initialization Prompt Created

File: `.claude/prompts/0022-initialize-drift-clock.md`
- Package rename instructions
- Navigation setup
- Theme configuration
- Data layer foundation
- MVP feature stubs

## Session Complete
Blueprint session completed successfully. Project initialized for Drift Clock.
