# Adaptive Screen Sizing

[no-commit]

## Overview

Updated Timer and Stopwatch screens to use adaptive sizing based on device dimensions.

## Changes Made

### Timer Screen
- Use `BoxWithConstraints` to get available width/height
- Calculate numpad button size based on available width (3 buttons + spacing)
- Button sizes clamped between 60dp-90dp for reasonable bounds
- Font size scales based on screen width (42sp-60sp)
- Use weight-based layout distribution:
  - Time display: 25% of height
  - Numpad: 55% of height
  - Start button: 20% of height
- Numpad fills available horizontal space appropriately

### Stopwatch Screen
- Use `BoxWithConstraints` for responsive layout
- Button sizes scale with screen width (17%-22%)
- Time font size scales (48sp-72sp based on width)
- Weight-based layout adapts to lap list presence:
  - Without laps: Time 45%, Controls 35%, Spacer 20%
  - With laps: Time 30%, Controls 20%, Laps 50%
- Lap list gets more space when present

## Technical Details

- Uses Compose `BoxWithConstraints` for constraint-aware layout
- All sizes calculated as proportions of available space
- Coerced min/max values prevent extreme sizes
- Layout smoothly adapts between different screen sizes

## Files Modified
- `ui/screens/timer/TimerScreen.kt`
- `ui/screens/stopwatch/StopwatchScreen.kt`
