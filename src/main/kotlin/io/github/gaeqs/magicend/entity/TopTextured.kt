package io.github.gaeqs.magicend.entity

import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

interface TopTextured {

    var topTexture: Identifier?

    var displayOffset: Vec3d

}