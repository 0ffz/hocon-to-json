import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check
import com.github.ajalt.clikt.parameters.types.path
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import kotlin.io.path.exists

class Cli : CliktCommand() {
    // make path the first argument
    val path by argument(help = "Path to the file").path()
        .check("File does not exist") { it.exists() }

    override fun run() {
        // Load config from path with hocon
        runCatching {
            val config = ConfigFactory.parseFile(path.toFile()).resolve()
            // Convert config to json
            config.root().render(ConfigRenderOptions.defaults().setComments(false).setOriginComments(false))
        }.onSuccess {
            echo(it)
        }.onFailure {
            echo("Error: ${it.message}", err = true)
        }
    }
}

fun main(args: Array<String>) = Cli().main(args)
