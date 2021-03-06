package gg.octave.bot.entities.framework

import gg.octave.bot.Launcher
import gg.octave.bot.db.OptionsRegistry
import me.devoxin.flight.api.entities.PrefixProvider
import net.dv8tion.jda.api.entities.Message

class DefaultPrefixProvider : PrefixProvider {
    override fun provide(message: Message): List<String> {
        val guildSettings = OptionsRegistry.ofGuild(message.guild)
        val prefixes = mutableListOf(
            "${message.jda.selfUser.name.toLowerCase()} ",
            "<@${message.jda.selfUser.id}> ",
            "<@!${message.jda.selfUser.id}> "
        )

        val customPrefix = guildSettings.command.prefix
            ?: Launcher.configuration.prefix

        prefixes.add(customPrefix)
        return prefixes.toList()
    }
}
