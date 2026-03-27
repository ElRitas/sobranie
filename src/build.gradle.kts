import org.jlleitschuh.gradle.ktlint.KtlintExtension
import java.io.File

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3" apply false
}

subprojects {
    // Исключаем UI модуль из проверок статического анализа
    if (project.name != "ui") {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
    }

    // Настройка проверок статического анализа только для не-UI модулей
    if (project.name != "ui") {
        extensions.configure<KtlintExtension> {
            android.set(
                plugins.hasPlugin("com.android.application") ||
                        plugins.hasPlugin("com.android.library") ||
                        plugins.hasPlugin("org.jetbrains.kotlin.android")
            )
        }

        tasks.matching { it.name == "check" }.configureEach {
            dependsOn("ktlintCheck")
        }
    }
}
