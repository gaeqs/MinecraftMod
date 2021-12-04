package io.github.gaeqs.magicend.block.entity

import io.github.gaeqs.magicend.block.EnderBreadPlateBlock
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound

class EnderBreadPlateBlockEntity : BlockEntity(EnderBreadPlateBlock.BLOCK_ENTITY), BlockEntityClientSerializable {

    private var _amount: Int = 0
    var amount: Int
        get() = _amount
        set(value) {
            _amount = value
            markDirty()
            if (!world!!.isClient) {
                sync()
            }
        }

    fun isFull() = _amount >= 9

    fun isEmpty() = _amount == 0

    override fun writeNbt(nbt: NbtCompound) = super.writeNbt(nbt).apply { putInt("amount", _amount) }

    override fun fromTag(state: BlockState, tag: NbtCompound) {
        super.fromTag(state, tag)
        _amount = tag.getInt("amount");
    }

    override fun fromClientTag(tag: NbtCompound) {
        _amount = tag.getInt("amount")
    }

    override fun toClientTag(tag: NbtCompound) = writeNbt(tag)


}