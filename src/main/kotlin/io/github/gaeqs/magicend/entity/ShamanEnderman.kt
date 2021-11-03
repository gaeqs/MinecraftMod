package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.defaults.tree.findWalkTarget
import io.github.gaeqs.magicend.ai.defaults.tree.walkToTarget
import io.github.gaeqs.magicend.ai.statemachine.StateMachineActivity
import io.github.gaeqs.magicend.ai.statemachine.builder.stateMachine
import io.github.gaeqs.magicend.ai.statemachine.node.lambda
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.random.Random
import kotlin.random.nextUInt

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

    private val ai = EntityAI(this)

    init {
        initAI()
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(TRACKED_TOP_MESSAGE, MESSAGE_1.toString())
    }

    override fun setAttacker(attacker: LivingEntity?) {
        super.setAttacker(attacker)
        topTexture = if (attacker == null) MESSAGE_1 else MESSAGE_2
    }

    override fun mobTick() {
        world.profiler.push("shamanBrain")
        ai.tick()
        world.profiler.pop()
    }

    private fun initAI() {
        ai.coreActivity = TreeActivity("core", ai, loopUnconditional {
            and {
                findWalkTarget(1.0f)

                succeeder {
                    timed(20, 40) {
                        walkToTarget()
                    }
                }

                lambda {
                    start {
                        println("START")
                    }
                    tick {
                        println("INVOKE!")
                        TreeNode.InvocationResult.SUCCESS
                    }
                    stop {
                        println("STOP")
                    }
                }

                wait(100)
            }
        })

        ai.activities += StateMachineActivity("idle", ai, stateMachine {
            lambda("start") {
                start { topTexture = MESSAGE_1 }
                tick {
                    if (Random.Default.nextInt(0, 20) == 0) {
                        changeState("state2")
                    }
                }
            }
            lambda("state2") {
                start { topTexture = MESSAGE_2 }
                tick {
                    if (Random.Default.nextInt(0, 20) == 0) {
                        changeState("start")
                    }
                }
            }
        })

    }

}