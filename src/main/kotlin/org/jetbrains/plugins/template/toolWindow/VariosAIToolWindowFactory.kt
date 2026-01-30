package org.jetbrains.plugins.template.toolWindow

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.content.ContentFactory
import org.jetbrains.plugins.template.MyBundle
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JTextField

class VariosAIToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val variosAIToolWindow = VariosAIToolWindow(project)
        val content = ContentFactory.getInstance().createContent(variosAIToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class VariosAIToolWindow(private val project: Project) {

        private val baseUrlField = JTextField("https://api.openai.com/v1/chat/completions", 30)
        private val apiKeyField = JTextField("", 30)
        private val modelIdField = JTextField("gpt-3.5-turbo", 20)
        private val promptArea = JBTextArea(10, 40)
        private val referenceFileCheckbox = JCheckBox(MyBundle.message("variosai.referenceFile"))
        private val responseArea = JBTextArea(15, 40)

        fun getContent() = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            val mainPanel = JBPanel<JBPanel<*>>(GridBagLayout())
            val gbc = GridBagConstraints()
            gbc.insets = Insets(5, 5, 5, 5)
            gbc.fill = GridBagConstraints.HORIZONTAL
            gbc.anchor = GridBagConstraints.WEST

            // Base URL
            gbc.gridx = 0
            gbc.gridy = 0
            mainPanel.add(JBLabel(MyBundle.message("variosai.baseUrl")), gbc)
            gbc.gridx = 1
            gbc.weightx = 1.0
            mainPanel.add(baseUrlField, gbc)

            // API Key
            gbc.gridx = 0
            gbc.gridy = 1
            gbc.weightx = 0.0
            mainPanel.add(JBLabel(MyBundle.message("variosai.apiKey")), gbc)
            gbc.gridx = 1
            gbc.weightx = 1.0
            mainPanel.add(apiKeyField, gbc)

            // Model ID
            gbc.gridx = 0
            gbc.gridy = 2
            gbc.weightx = 0.0
            mainPanel.add(JBLabel(MyBundle.message("variosai.modelId")), gbc)
            gbc.gridx = 1
            gbc.weightx = 1.0
            mainPanel.add(modelIdField, gbc)

            // Prompt area
            gbc.gridx = 0
            gbc.gridy = 3
            gbc.weightx = 0.0
            mainPanel.add(JBLabel(MyBundle.message("variosai.prompt")), gbc)
            gbc.gridx = 1
            gbc.weightx = 1.0
            gbc.fill = GridBagConstraints.BOTH
            gbc.weighty = 1.0
            promptArea.lineWrap = true
            promptArea.wrapStyleWord = true
            mainPanel.add(JBScrollPane(promptArea), gbc)

            // Reference file checkbox
            gbc.gridy = 4
            gbc.weighty = 0.0
            gbc.fill = GridBagConstraints.HORIZONTAL
            mainPanel.add(referenceFileCheckbox, gbc)

            // Submit button
            gbc.gridy = 5
            val submitButton = JButton(MyBundle.message("variosai.submit"))
            submitButton.addActionListener {
                submitPrompt()
            }
            mainPanel.add(submitButton, gbc)

            // Response area
            gbc.gridy = 6
            gbc.weighty = 2.0
            gbc.fill = GridBagConstraints.BOTH
            responseArea.lineWrap = true
            responseArea.wrapStyleWord = true
            responseArea.isEditable = false
            mainPanel.add(JBScrollPane(responseArea), gbc)

            add(mainPanel, BorderLayout.CENTER)
        }

        private fun submitPrompt() {
            val baseUrl = baseUrlField.text.trim()
            val apiKey = apiKeyField.text.trim()
            val modelId = modelIdField.text.trim()
            var promptContent = promptArea.text.trim()

            // Validation
            if (baseUrl.isEmpty()) {
                Messages.showErrorDialog(project, MyBundle.message("variosai.error.baseUrlRequired"), MyBundle.message("variosai.error.title"))
                return
            }
            if (apiKey.isEmpty()) {
                Messages.showErrorDialog(project, MyBundle.message("variosai.error.apiKeyRequired"), MyBundle.message("variosai.error.title"))
                return
            }
            if (modelId.isEmpty()) {
                Messages.showErrorDialog(project, MyBundle.message("variosai.error.modelIdRequired"), MyBundle.message("variosai.error.title"))
                return
            }
            if (promptContent.isEmpty()) {
                Messages.showErrorDialog(project, MyBundle.message("variosai.error.promptRequired"), MyBundle.message("variosai.error.title"))
                return
            }

            // Add file content if referenced
            if (referenceFileCheckbox.isSelected) {
                val fileContent = getOpenedFileContent()
                if (fileContent != null) {
                    promptContent = "$promptContent\n\nFile content:\n$fileContent"
                }
            }

            // Make API call
            try {
                responseArea.text = MyBundle.message("variosai.sending")
                val response = callVariosAI(baseUrl, apiKey, modelId, promptContent)
                responseArea.text = response
            } catch (e: Exception) {
                responseArea.text = "${MyBundle.message("variosai.error.apiCall")}\n${e.message}"
                Messages.showErrorDialog(project, e.message ?: "Unknown error", MyBundle.message("variosai.error.title"))
            }
        }

        private fun getOpenedFileContent(): String? {
            val fileEditorManager = FileEditorManager.getInstance(project)
            val selectedEditor = fileEditorManager.selectedTextEditor
            return selectedEditor?.document?.text
        }

        private fun callVariosAI(baseUrl: String, apiKey: String, modelId: String, prompt: String): String {
            val url = URL(baseUrl)
            val connection = url.openConnection() as HttpURLConnection
            
            try {
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", "Bearer $apiKey")
                connection.doOutput = true

                val requestBody = """
                    {
                        "model": "${escapeJson(modelId)}",
                        "messages": [{"role": "user", "content": "${escapeJson(prompt)}"}]
                    }
                """.trimIndent()

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(requestBody)
                    writer.flush()
                }

                val responseCode = connection.responseCode
                
                val responseText = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error details"
                    throw Exception("HTTP $responseCode: $errorText")
                }
                
                return responseText
                
            } finally {
                connection.disconnect()
            }
        }

        private fun escapeJson(text: String): String {
            return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
        }
    }
}
