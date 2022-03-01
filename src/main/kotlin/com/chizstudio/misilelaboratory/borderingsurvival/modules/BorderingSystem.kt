package com.chizstudio.misilelaboratory.borderingsurvival.modules

import com.chizstudio.misilelaboratory.borderingsurvival.BorderingSurvival
import io.github.monun.kommand.kommand
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.WorldBorder
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

data class Config(
    var radius: Double,
    var locationmin: Int,
    var locationmax: Int
    )

class BorderingSystem(private val plugin: BorderingSurvival) {

    private val eventhandler = BorderingListener()
    private val config = Config((50).toDouble(), -50, 50) // will change with command
    private val timeInSeconds = 50L
    private var BorderingBorder: WorldBorder? = null
    private var bossbar = plugin.server.getBossBar(NamespacedKey.fromString("timer")!!) as BossBar?
    init {
        plugin.server.pluginManager.registerEvents(eventhandler, plugin)
        if (bossbar == null) {
            bossbar = plugin.server.createBossBar("청크 변동까지 남은 시간: ", BarColor.GREEN, BarStyle.SOLID)
        }
        bossbar?.isVisible = false
    }

    fun changeBorderWithRandom(border: WorldBorder) {
        changeBorder(((config.locationmin..config.locationmax).random()).toDouble(), border)
    }

    fun changeBorder(locationint: Double, border: WorldBorder) {
        val newX = border.center.x + locationint
        val newZ = border.center.z + locationint
        val bs = Bukkit.getScheduler()
        val stepsTime = timeInSeconds * 20
        val st = stepsTime / 5
        val centerX = border.center.x
        val centerZ = border.center.z
        val gapX: Double = if (newX < centerX) centerX - newX else newX - centerX
        val gapZ: Double = if (newZ < centerZ) centerZ - newZ else newZ - centerZ
        val stepsX = gapX / st
        val stepsZ = gapZ / st
        border.setSize(config.radius, timeInSeconds)
        for (i in 1..st) {
            if (newX < centerX) {
                val n: Double = centerX - i * stepsX
                bs.runTaskLaterAsynchronously(plugin,
                    Runnable { border.setCenter(n, border.center.z) }, (i * 5)
                )
            } else {
                val n: Double = centerX + i * stepsX
                bs.runTaskLaterAsynchronously(plugin,
                    Runnable { border.setCenter(n, border.center.z) }, (i * 5)
                )
            }
        }
        for (i in 1..st) {
            if (newZ < centerZ) {
                val n: Double = centerZ - i * stepsZ
                bs.runTaskLaterAsynchronously(plugin,
                    Runnable { border.setCenter(border.center.x, n) }, (i * 5)
                )
            } else {
                val n: Double = centerZ + i * stepsZ
                bs.runTaskLaterAsynchronously(plugin,
                    Runnable { border.setCenter(border.center.x, n) }, (i * 5)
                )
            }
        }
    }

    fun setupKommand(BorderSystem: BorderingSystem) {
        plugin.kommand {
            register("BorderSurvival") {
                then("start") {
                    executes {
                        val player = sender as Player
                        BorderSystem.BorderingBorder = player.world.worldBorder
                    }
                }
            }
        }
    }

}