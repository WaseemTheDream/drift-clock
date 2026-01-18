# Design Guidelines: Drift Clock

## Design Philosophy
**Stealth is the feature.** Drift Clock must be visually indistinguishable from the stock Android Clock app. Every pixel should feel native.

## Style
- **Material Design 3** (Google's latest design system)
- Match stock Android Clock app exactly
- No custom branding that breaks the illusion

## Colors

### Primary Palette (Stock Android Clock)
- **Primary**: Google Blue (`#4285F4` or Material Blue)
- **Surface**: System default (light/dark mode aware)
- **On Surface**: System default text colors

### Theme Support
- Support both light and dark mode
- Follow system theme setting
- Colors should match stock Clock in both modes

## Typography
- **System default** (Roboto)
- Match stock Clock font sizes and weights
- Large display numbers for timer/stopwatch

## Navigation

### Bottom Navigation (5 Tabs)
```
[ Alarm ] [ Clock ] [ Timer ] [ Stopwatch ] [ Bedtime ]
```

- Icons and labels must match stock Android Clock exactly
- Timer tab selected by default (or last used)
- Smooth tab transitions

## Component Guidelines

### Timer UI
- Large circular or linear time display
- Numeric input for setting time
- Prominent Start/Pause button
- Reset option
- Match stock Clock timer exactly

### Stopwatch UI
- Large time display (MM:SS.ss)
- Lap list below
- Start/Stop and Lap buttons
- Match stock Clock stopwatch exactly

### Profile Selection (First Launch)
- Full-screen modal/page
- Clean card-based profile options
- Profile name prominent
- Subtext description below
- Single tap to select and proceed

### Hidden Settings
- Accessible via Bedtime tab
- Subtle entry point (long-press or specific gesture)
- Clean settings UI once accessed
- Slider for custom speed
- Profile quick-switch

## Iconography
- Use Material Icons matching stock Clock
- Tab bar icons: alarm, clock, timer, stopwatch, bedtime/moon

## Animation
- Standard Material motion
- No flashy or attention-grabbing animations
- Smooth, native-feeling transitions

## Inspiration
- Stock Android Clock app (primary and only reference)
- Goal: Users shouldn't realize it's not the real Clock app

## Accessibility
- Standard accessibility support
- Content descriptions for screen readers
- Adequate touch targets
- Color contrast compliance
