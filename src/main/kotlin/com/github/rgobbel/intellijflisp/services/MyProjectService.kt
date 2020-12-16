package com.github.rgobbel.intellijflisp.services

import com.intellij.openapi.project.Project
import com.github.rgobbel.intellijflisp.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
