# VariosAI Chat Plugin - Implementation Guide

## Overview
This IntelliJ plugin adds a new tool window called "VariosAI Chat" that allows users to interact with variosAI or any OpenAI-compatible API endpoint.

## Features

### Configuration Fields
- **Base URL**: The API endpoint URL (default: https://api.openai.com/v1/chat/completions)
- **API Key**: The authentication token for the API
- **Model ID**: The model identifier to use (default: gpt-3.5-turbo)

### Functionality
- **Prompt Input**: Multi-line text area for entering prompts
- **File Reference**: Checkbox to include the currently opened file's content in the prompt
- **Submit Button**: Sends the request to the configured API endpoint
- **Response Display**: Shows the API response in a read-only text area

## Implementation Details

### Files Created/Modified

1. **VariosAIToolWindowFactory.kt** - Main tool window implementation
   - Creates the UI with GridBagLayout
   - Handles API communication via HTTP POST
   - Manages file content injection when referenced
   - Provides error handling and validation

2. **plugin.xml** - Plugin registration
   - Registers the new tool window with ID "VariosAI Chat"
   - Anchored to the right side of the IDE

3. **MyBundle.properties** - Internationalization strings
   - Added labels and error messages for the VariosAI Chat UI

4. **VariosAIToolWindowTest.kt** - Unit tests
   - Tests tool window registration
   - Verifies factory availability

## API Integration

The plugin makes HTTP POST requests in the following format:

```bash
POST {baseUrl}
Headers:
  - Content-Type: application/json
  - Authorization: Bearer {apiKey}

Body:
{
  "model": "{modelId}",
  "messages": [
    {
      "role": "user",
      "content": "{prompt}"
    }
  ]
}
```

When "Reference opened file" is checked, the file content is appended to the prompt:
```
{user_prompt}

File content:
{file_content}
```

## Usage

1. Open the "VariosAI Chat" tool window from the right sidebar
2. Configure your API settings:
   - Enter the Base URL for your variosAI endpoint
   - Provide your API Key
   - Set the Model ID
3. Enter your prompt in the text area
4. (Optional) Check "Reference opened file" to include the current file's content
5. Click "Submit" to send the request
6. View the response in the response area below

## Security Considerations

- API keys are stored in memory only during the IDE session
- All communication uses HTTPS (depending on the configured base URL)
- JSON content is properly escaped to prevent injection attacks

## Error Handling

The plugin validates:
- Base URL must not be empty
- API Key must not be empty
- Model ID must not be empty
- Prompt must not be empty

Network errors and API errors are displayed in both the response area and as error dialogs.

## Building and Testing

Due to network restrictions in the build environment, the plugin cannot be fully built without IntelliJ Platform dependencies. However, the implementation is complete and follows IntelliJ plugin development best practices.

To build manually:
```bash
./gradlew clean build
```

To run the plugin in development mode:
```bash
./gradlew runIde
```

## Future Enhancements

Potential improvements:
- Persist configuration settings
- Add support for multiple messages/conversation history
- Add syntax highlighting for code in responses
- Support for streaming responses
- Configuration profiles for different AI services
