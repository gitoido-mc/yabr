package lol.gito.yabr.service.action

import lol.gito.yabr.Yabr
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings

class PlaceStructure(
    val structure: Identifier,
    val level: ServerLevel,
    val pos: BlockPos,
) : ActionInterface {
    override fun run() {
        val structTemplateManager = level.server.structureTemplateManager
        val structureTemplate = structTemplateManager.get(structure)

        if (structureTemplate != null) {
            val structPlacementData =
                StructurePlaceSettings()
                    .setIgnoreEntities(true)
                    .setMirror(Mirror.NONE)
                    .setRotation(Rotation.NONE)
                    .setKnownShape(true)

            if (!structureTemplate.get().placeInWorld(level, pos.atY(256), pos, structPlacementData, level.random, Block.UPDATE_ALL)) {
                Yabr.info("Error placing structure: $structure")
            } else {
                Yabr.info("Successfully placed structure: $structure at ${pos.x},${pos.y},${pos.z}")
            }
        }
    }
}
