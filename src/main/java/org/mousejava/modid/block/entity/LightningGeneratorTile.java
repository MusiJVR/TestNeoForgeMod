package org.mousejava.modid.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.mousejava.modid.init.TilesMI;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltip;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;
import org.zeith.hammerlib.util.java.DirectStorage;

public class LightningGeneratorTile extends TileSyncableTickable implements IEnergyStorage, ITooltipProvider {
    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> this);

    private final int maxEnergy = 16000;
    private final int maxTransfer = 500;

    @NBTSerializable
    private int currentEnergy = 0;

    private final PropertyInt energy = new PropertyInt(DirectStorage.create(i -> currentEnergy = i, () -> currentEnergy));

    public LightningGeneratorTile(BlockPos pos, BlockState state) {
        super(TilesMI.LIGHTNING_GENERATOR_TILE, pos, state);
        this.dispatcher.registerProperty("energy", energy);
    }

    @Override
    public void serverTick() {
        if (this.level != null) {
            for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(8))) {
                if (entity instanceof LightningBolt) {
                    if (!entity.getTags().contains("lightningProcessed")) {
                        generateEnergyFromLightning(entity);
                        entity.addTag("lightningProcessed");
                    }
                }
            }
        }

        spreadEnergyToNeighbors();
    }

    @Override
    public void clientTick() {
        setTooltipDirty(true);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCap.cast();
        }
        return super.getCapability(cap, side);
    }

    private void generateEnergyFromLightning(Entity lightning) {
        double distance = this.worldPosition.distSqr(new Vec3i((int) lightning.getX(), (int) lightning.getY(), (int) lightning.getZ()));
        int energyGenerated = (int) (maxEnergy * (1 - Math.min(distance / 64.0, 1)));
        energy.setInt(Math.min(currentEnergy + energyGenerated, maxEnergy));
    }

    private void spreadEnergyToNeighbors() {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = this.worldPosition.relative(direction);
            BlockEntity neighborTile  = this.level.getBlockEntity(neighborPos);

            if (neighborTile == null) continue;

            LazyOptional<IEnergyStorage> energyCap = neighborTile.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
            energyCap.ifPresent(storage -> {
                int energyReceived = storage.receiveEnergy(Math.min(currentEnergy, maxTransfer), false);
                extractEnergy(energyReceived, false);
            });
        }
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate) {
        if (!canReceive()) return 0;

        int energyReceived = Math.min(maxEnergy - currentEnergy, Math.min(energy, maxTransfer));
        if (!simulate) {
            this.energy.setInt(currentEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate) {
        if (!canExtract()) return 0;

        int energyExtracted = Math.min(currentEnergy, Math.min(energy, maxTransfer));
        if (!simulate) {
            this.energy.setInt(currentEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return currentEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    public boolean dirty;

    @Override
    public boolean isTooltipDirty() {
        return dirty;
    }

    @Override
    public void setTooltipDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void addInformation(ITooltip tip) {
        tip.addText(Component.translatable("tooltip.lightning_generator.energy", currentEnergy));
    }
}
