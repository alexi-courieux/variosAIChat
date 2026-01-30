# VariosAI Chat Plugin

[![Build](https://github.com/alexi-courieux/variosAIChat/workflows/Build/badge.svg)][gh:build]

![VariosAI Chat Plugin]

> An IntelliJ IDEA plugin that provides a tool window for interacting with variosAI or any OpenAI-compatible API.

<!-- Plugin description -->
**VariosAI Chat** is an IntelliJ IDEA plugin that adds a dedicated tool window for AI-powered assistance. Configure your own AI endpoint (variosAI or any OpenAI-compatible API) and interact with it directly from your IDE. The plugin supports referencing your currently open file in prompts, making it perfect for code reviews, explanations, and enhancements.

## Features

- 🔧 **Configurable API Connection**: Set your own base URL, API key, and model ID
- 💬 **Interactive Chat Interface**: Multi-line prompt input with response display
- 📄 **File Reference**: Optionally include the currently opened file's content in your prompts
- 🔒 **Secure**: API keys are masked and never persisted to disk
- ⚡ **Non-blocking**: All API calls run on background threads to keep the IDE responsive
- ✅ **Error Handling**: Comprehensive validation and user-friendly error messages

## Installation

### From Source

1. Clone this repository
2. Run `./gradlew buildPlugin`
3. Install the plugin from disk: `build/distributions/VariosAI-Chat-*.zip`

### From JetBrains Marketplace

Coming soon!

## Usage

1. Open the **VariosAI Chat** tool window from the right sidebar
2. Configure your API settings:
   - **Base URL**: Your variosAI endpoint (e.g., `https://api.openai.com/v1/chat/completions`)
   - **API Key**: Your authentication token
   - **Model ID**: The model to use (e.g., `gpt-3.5-turbo`)
3. Enter your prompt in the text area
4. (Optional) Check "Reference opened file" to include the current file's content
5. Click **Submit** to send your request
6. View the response in the area below

## API Format

The plugin sends HTTP POST requests in this format:

```bash
POST {baseUrl}
Headers:
  Content-Type: application/json
  Authorization: Bearer {apiKey}

Body:
{
  "model": "{modelId}",
  "messages": [
    {"role": "user", "content": "{prompt}"}
  ]
}
```

When "Reference opened file" is checked, the file content is appended to the prompt.

## Documentation

- [SUMMARY.md](SUMMARY.md) - Complete implementation overview and features
- [VARIOSAI_IMPLEMENTATION.md](VARIOSAI_IMPLEMENTATION.md) - Technical implementation guide
- [UI_LAYOUT.md](UI_LAYOUT.md) - Visual UI documentation and workflow

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "variosAIChat"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/alexi-courieux/variosAIChat/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
