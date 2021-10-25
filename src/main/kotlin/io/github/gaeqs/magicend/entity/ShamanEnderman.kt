package io.github.gaeqs.magicend.entity

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.ai.brain.Activity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.task.FollowMobTask
import net.minecraft.entity.ai.brain.task.Task
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ShamanEnderman(type: EntityType<out PathAwareEntity>, world: World) : PathAwareEntity(type, world), TopTextured {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "shaman_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<ShamanEnderman> { type, world -> ShamanEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        private val TRACKED_TOP_MESSAGE =
            DataTracker.registerData(ShamanEnderman::class.java, TrackedDataHandlerRegistry.STRING)
        private val MESSAGE_1 = Identifier(MinecraftMod.MOD_ID, "textures/entity/example/example_message.png")
        private val MESSAGE_2 = Identifier(MinecraftMod.MOD_ID, "textures/entity/example/example_message_2.png")

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    override var topTexture: Identifier?
        get() {
            val string = dataTracker.get(TRACKED_TOP_MESSAGE)
            return if (string.isNullOrEmpty()) null else Identifier(string)
        }
        set(value) {
            dataTracker.set(TRACKED_TOP_MESSAGE, value?.toString() ?: "")
        }

    override var displayOffset = Vec3d(0.0, -2.5, 0.0)

    init {
        initBrain()
    }


    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(TRACKED_TOP_MESSAGE, MESSAGE_1.toString())
    }

    override fun setAttacker(attacker: LivingEntity?) {
        super.setAttacker(attacker)
        topTexture = if (attacker == null) MESSAGE_1 else MESSAGE_2
    }

    override fun tick() {
        super.tick()
        println(brain)
    }


    override fun getBrain(): Brain<ShamanEnderman> {
        return super.getBrain() as Brain<ShamanEnderman>
    }

    private fun initBrain() {
        val brain = getBrain();

        brain.setTaskList(Activity.IDLE, ImmutableList.of(createFreeFollowTask()))
        brain.setDefaultActivity(Activity.IDLE)
        brain.doExclusively(Activity.IDLE)
    }

    private fun createFreeFollowTask(): Pair<Int, Task<LivingEntity?>>? {
        return Pair.of(
            5,
            FollowMobTask(EntityType.CAT, 8.0f)
        )
    }

}