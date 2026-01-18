# Timer Numpad UI Update

[auto-commit]

## Overview

Updated the Timer screen to use a numpad-style input matching the stock Android Clock app.

## Changes Made

### Timer Input Display
- Added `00h 00m 00s` format display at the top
- Numbers fill in from right to left (seconds first, then minutes, then hours)
- Inactive digits appear faded, active digits are highlighted
- Unit labels (h, m, s) shown in primary color

### Number Pad
- 3x4 grid of circular buttons
- Layout: 1-9, then 00, 0, backspace
- Circular shape with `surfaceContainer` background
- Clean typography matching stock Clock

### Interaction Flow
1. User taps digits on numpad
2. Digits fill into the display from right to left
3. Display shows `00h 00m 00s` updating as user types
4. Backspace removes last digit
5. Start button enabled when valid input exists

## Technical Details

- Max 6 digits input (HHMMSS)
- Input stored as string, parsed when starting timer
- Display updates reactively as digits are entered
- Smooth transition to countdown view when timer starts

## Commit
Commit: `[0004] feat: Update Timer screen with numpad-style input`
