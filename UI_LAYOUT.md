# VariosAI Chat - UI Layout

## Tool Window Structure

```
┌─────────────────────────────────────────────────────────┐
│ VariosAI Chat                                      [×]  │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Base URL:     [https://api.openai.com/v1/chat/comp... │
│                                                         │
│  API Key:      [••••••••••••••••••••••••••••••••••]    │
│                                                         │
│  Model ID:     [gpt-3.5-turbo                      ]    │
│                                                         │
│  Prompt:       ┌────────────────────────────────────┐  │
│                │                                    │  │
│                │  Enter your prompt here...         │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                └────────────────────────────────────┘  │
│                                                         │
│                [✓] Reference opened file                │
│                                                         │
│                [ Submit ]                               │
│                                                         │
│  Response:     ┌────────────────────────────────────┐  │
│                │                                    │  │
│                │  API response will appear here... │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                │                                    │  │
│                └────────────────────────────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## UI Components

### Input Fields
1. **Base URL** (JTextField)
   - Default: `https://api.openai.com/v1/chat/completions`
   - Editable text field for API endpoint

2. **API Key** (JPasswordField)
   - Masked input (shows •••)
   - Secure credential entry
   - Not persisted

3. **Model ID** (JTextField)
   - Default: `gpt-3.5-turbo`
   - Editable text field for model selection

### Prompt Input
4. **Prompt** (JBTextArea)
   - Multi-line text area
   - Word wrap enabled
   - Scrollable
   - Expandable height

### Options
5. **Reference opened file** (JCheckBox)
   - When checked, appends current file content to prompt
   - Optional feature

### Actions
6. **Submit** (JButton)
   - Sends request to API
   - Disabled during processing
   - Shows loading message

### Output
7. **Response** (JBTextArea)
   - Read-only display
   - Multi-line with word wrap
   - Scrollable
   - Shows API response or errors

## User Workflow

```
┌─────────────┐
│   Start     │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ Configure Settings  │
│ - Base URL          │
│ - API Key          │
│ - Model ID         │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Enter Prompt        │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐      Yes      ┌──────────────────┐
│ Reference file?     │──────────────▶│ Append file      │
└──────┬──────────────┘               │ content to prompt│
       │ No                            └────────┬─────────┘
       │                                        │
       │                                        │
       ▼                                        │
┌─────────────────────┐                        │
│ Click Submit        │◀───────────────────────┘
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Button Disabled     │
│ "Sending request..."│
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Network Request     │
│ (Background Thread) │
└──────┬──────────────┘
       │
       ├────────────┬────────────┐
       │            │            │
       ▼            ▼            ▼
  ┌────────┐  ┌─────────┐  ┌──────────┐
  │Success │  │ Timeout │  │ HTTP Err │
  └────┬───┘  └────┬────┘  └────┬─────┘
       │           │            │
       └───────────┴────────────┘
                   │
                   ▼
       ┌───────────────────────┐
       │ Display Response      │
       │ Re-enable Submit      │
       └───────────────────────┘
```

## Layout Details

### GridBagLayout Configuration
- **Insets**: 5px padding on all sides
- **Fill**: HORIZONTAL for inputs, BOTH for text areas
- **Anchor**: WEST for labels
- **Weight**: 
  - Prompt area: weighty=1.0
  - Response area: weighty=2.0 (larger)

### Positioning
- Tool window: Anchored to RIGHT sidebar
- Layout: Vertical stack with proper spacing
- Scrolling: Text areas have JBScrollPane wrappers

## Accessibility Features
- All fields have associated labels
- Password field for sensitive data
- Clear error messages via dialogs
- Read-only response area (prevents accidental edits)
- Proper tab order for keyboard navigation

## Visual Feedback
- **Loading**: "Sending request..." message
- **Processing**: Submit button disabled
- **Success**: Response displayed in text area
- **Error**: Error message in response area + error dialog
- **Validation**: Error dialogs for missing required fields
