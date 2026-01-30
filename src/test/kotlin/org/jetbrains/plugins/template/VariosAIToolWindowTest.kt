package org.jetbrains.plugins.template

import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.plugins.template.toolWindow.VariosAIToolWindowFactory

class VariosAIToolWindowTest : BasePlatformTestCase() {

    fun testToolWindowRegistration() {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("VariosAI Chat")
        
        assertNotNull("VariosAI Chat tool window should be registered", toolWindow)
    }

    fun testToolWindowFactory() {
        val factory = VariosAIToolWindowFactory()
        
        assertTrue("Factory should be available for the project", factory.shouldBeAvailable(project))
    }
}
