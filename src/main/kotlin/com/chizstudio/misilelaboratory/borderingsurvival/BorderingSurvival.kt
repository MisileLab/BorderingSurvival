package com.chizstudio.misilelaboratory.borderingsurvival

import org.bukkit.plugin.java.JavaPlugin

class BorderingSurvival: JavaPlugin() {
    override fun onEnable() {
        logger.info("Starting BorderingSurvival Plugin...")
    }

    override fun onDisable() {
        logger.info("Stopping BorderingSurvival Plugin...")
    }
}