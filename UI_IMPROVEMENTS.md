# UI Modernization and Responsive Design

## Overview
This document describes the UI improvements made to make Aries more modern and responsive, particularly for tiling window managers like Hyprland and Niri.

## Key Improvements

### 1. Responsive Layout System
The UI now uses `BoxWithConstraints` to dynamically adapt to window size changes:

- **Compact Mode** (< 600dp width)
  - Single column vertical layout
  - Scrollable content
  - Smaller padding (12dp)
  - Condensed typography (h6 instead of h5, caption instead of body2)
  - Vertical button/input stacking

- **Medium Mode** (600dp - 900dp width)
  - Side-by-side two-column layout
  - Settings on left, Commands on right
  - Medium padding (16dp)
  - Equal weight distribution (1:1)

- **Expanded Mode** (> 900dp width)
  - Side-by-side two-column layout
  - Settings on left (45%), Commands on right (55%)
  - Maximum padding (24dp)
  - Full-size typography

### 2. Window Management
- Default window size: 900x700dp
- Minimum window size recommended: 400x500dp
- Components gracefully reflow and resize
- No fixed sizes - everything uses flexible layouts

### 3. Dark Theme Support
Two color schemes available (currently defaulting to dark):

#### Dark Theme
- Primary: #64B5F6 (Light Blue)
- Background: #121212
- Surface: #1E1E1E
- Text: #E1E1E1

#### Light Theme
- Primary: #2196F3 (Blue)
- Background: #F5F5F5
- Surface: #FFFFFF
- Text: #212121

### 4. Component Improvements

#### Dropdown Menus
- Modern card-style design with rounded corners (12dp)
- Elevation changes on interaction (3dp → 6dp)
- Background highlight when expanded
- Improved option selection visualization
- Better search functionality with clear button
- Responsive width (320dp max)

#### Buttons
- Rounded corners (10dp)
- Multiple elevation states:
  - Default: 3dp
  - Hovered: 4dp
  - Pressed: 6dp
- SemiBold font weight
- Adequate touch targets (52-56dp height)

#### Text Fields
- Rounded corners (10dp)
- Background color differentiation
- Enhanced focus indicators
- Medium font weight labels
- Better unfocused state visibility

#### Cards
- Variable elevation based on size/importance:
  - Header: 2-4dp
  - Content: 1-2dp
- Rounded corners (8-12dp based on mode)
- Proper spacing and padding

### 5. Typography
- Scales with window size
- Bold headers for better hierarchy
- SemiBold buttons for emphasis
- Adequate line height for readability

### 6. Spacing System
Adaptive spacing based on window size:
- Compact: 12dp between major elements
- Medium: 16dp between major elements
- Expanded: 20-24dp between major elements

## Testing Window Sizes

### Recommended Test Scenarios

1. **Minimum Viable Size**: 400x500dp
   - Compact mode active
   - Scrollable content
   - All elements accessible

2. **Tablet/Small Desktop**: 600x700dp
   - Medium mode active
   - Side-by-side layout
   - Balanced proportions

3. **Standard Desktop**: 900x700dp
   - Expanded mode active
   - Optimal viewing experience
   - Full feature visibility

4. **Wide Desktop**: 1200x800dp
   - Expanded mode active
   - Spacious layout
   - Maximum comfort

### Tiling Window Manager Compatibility

The UI has been specifically designed for Hyprland and Niri:

- **Dynamic Sizing**: Uses BoxWithConstraints to respond to any window size
- **Flexible Layout**: No hardcoded dimensions, everything uses weight/fill
- **Scrollable Sections**: Content doesn't get cut off at small sizes
- **Adaptive Padding**: Reduces wasted space in compact mode
- **Reflow Support**: Components reorganize based on available space

## Code Structure

### Main Components

1. **ComposableGUI** (ComposableGUI.kt)
   - Main window container
   - Responsive layout logic
   - Window state management

2. **HeaderCard** (ComposableGUI.kt)
   - App title and description
   - Adaptive typography

3. **SettingsCard** (ComposableGUI.kt)
   - Voice preferences dropdowns
   - Max words input
   - Apply button
   - Vertical scroll support

4. **CommandsCard** (ComposableGUI.kt)
   - Command reference display
   - Scrollable content
   - Compact text in small mode

5. **ScrollableDropdownMenu** (Composable.kt)
   - Enhanced dropdown component
   - Search functionality
   - Modern styling

## Future Enhancements

Potential improvements for consideration:

1. User preference for light/dark theme
2. Custom accent color selection
3. Font size scaling option
4. Keyboard navigation improvements
5. Animation transitions between states
6. Persistent window size/position
