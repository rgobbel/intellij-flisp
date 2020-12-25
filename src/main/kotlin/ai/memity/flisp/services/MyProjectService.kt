package ai.memity.flisp.services

import com.intellij.openapi.project.Project
import ai.memity.flisp.FlispBundle

class MyProjectService(project: Project) {

    init {
        println(FlispBundle.message("projectService", project.name))
    }
}
