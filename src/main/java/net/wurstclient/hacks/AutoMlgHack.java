package net.wurstclient.hacks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.wurstclient.SearchTags;
import net.wurstclient.WurstClient;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;

@SearchTags(
{ "MLG", "Bucket", "Water", "Fall" })
public class AutoMlgHack extends Hack implements UpdateListener
{
	// Better guess the landing spot: something with velocity

	public AutoMlgHack()
	{
		super("AutoMlgHack");
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = WurstClient.MC.player;

		if (player.isOnGround())
			return;
		if (player.fallDistance <= (float) player.getSafeFallDistance())
			return;
		if (!isFallingFastEnoughToCauseDamage(player))
			return;

		LandingPosition top = getPlayersLandingPosition(player);

		BlockState topState = player.world.getChunk(top.getBlockPos()).getBlockState(top.getBlockPos().down());

		// Water is already placed, or was there
		if (topState.getMaterial() == Material.WATER)
			return;

		int distanceToGround = player.getBlockPos().getY() - top.getBlockPos().getY();

		if (distanceToGround <= 5)
		{
			int slot = player.getInventory().getSlotWithStack(new ItemStack(Items.WATER_BUCKET));

			if (slot < 0 && slot > 9)
				return;

			player.getInventory().selectedSlot = slot;

			// Look at top
			WurstClient.MC.player.lookAt(EntityAnchor.EYES, top.getLookPos());

			WurstClient.IMC.rightClick();
		}
	}

	private boolean isFallingFastEnoughToCauseDamage(AbstractClientPlayerEntity player)
	{
		return player.getVelocity().y < -0.5;
	}

	/**
	 * Gets the first block underneath the player. When player is on multiple
	 * blocks, it will return the highest.
	 * 
	 * @param player
	 * @return
	 */
	private LandingPosition getPlayersLandingPosition(AbstractClientPlayerEntity player)
	{
		double dx = player.getX() - player.getBlockX();
		double dz = player.getZ() - player.getBlockZ();

		BlockPos blockPos = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, player.getBlockPos());
		BlockPos temp;
		Vec3d look = new Vec3d(player.getX(), blockPos.getY(), player.getZ());

		if (dx > 0.7d && dz >= 0.3d && dz <= 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(1, 0, 0));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(temp.getX() + 0.05d, temp.getY(), look.getZ());
				blockPos = temp;
			}
		} else if (dx < 0.3d && dz >= 0.3d && dz <= 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(-1, 0, 0));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(blockPos.getX() - 0.05d, temp.getY(), look.getZ());
				blockPos = temp;
			}
		} else if (dz > 0.7d && dx >= 0.3d && dx <= 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(0, 0, 1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(look.getX(), temp.getY(), temp.getZ() + 0.05d);
				blockPos = temp;
			}
		} else if (dz < 0.3d && dx >= 0.3d && dx <= 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(0, 0, -1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(look.getX(), temp.getY(), blockPos.getZ() - 0.05d);
				blockPos = temp;
			}
		} else if (dx > 0.7d && dz > 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(1, 0, 1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(blockPos.getX() + 1.05d, temp.getY(), blockPos.getZ() + 1.05d);
				blockPos = temp;
			}
		} else if (dx > 0.7d && dz < 0.3d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(1, 0, -1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(blockPos.getX() + 1.05d, temp.getY(), blockPos.getZ() - 0.05d);
				blockPos = temp;
			}
		} else if (dx < 0.3d && dz > 0.7d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(-1, 0, 1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(blockPos.getX() - 0.05d, temp.getY(), blockPos.getZ() + 1.05d);
				blockPos = temp;
			}
		} else if (dx < 0.3d && dz < 0.3d)
		{
			temp = player.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(-1, 0, -1));
			if (temp.getY() > blockPos.getY())
			{
				look = new Vec3d(blockPos.getX() - 0.05d, temp.getY(), blockPos.getZ() - 0.05d);
				blockPos = temp;
			}
		}

		return new LandingPosition(look, blockPos);
	}

	private class LandingPosition
	{

		private Vec3d lookPos;
		private BlockPos blockPos;

		public LandingPosition(Vec3d lookPos, BlockPos blockPos)
		{
			this.lookPos = lookPos;
			this.blockPos = blockPos;
		}

		public BlockPos getBlockPos()
		{
			return blockPos;
		}

		public Vec3d getLookPos()
		{
			return lookPos;
		}

	}
}
