package net.wurstclient.hacks;

import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.WurstClient;
import net.wurstclient.events.EntitySpawnListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.ChatUtils;

@SearchTags({"Slime chunk finder"})
public class SlimeChunkFinderHack extends Hack implements EntitySpawnListener {
	
	/*
	 * TODO:
	 * 2 options: 	show in chat location of slime spawn/chunk
	 * 				or render chunk green (some flat bottom or top or ...)
	 * 
	 */
	
	public SlimeChunkFinderHack() {
		super("SlimeChunkFinder");
		setCategory(Category.CHAT);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(EntitySpawnListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(EntitySpawnListener.class, this);
	}

	@Override
	public void onEntitySpawn(EntitySpawnEvent event) {
		MobSpawnS2CPacket packet = event.getEntitySpawnPacket();
		
		if (packet.getEntityTypeId() != Registry.ENTITY_TYPE.getRawId(EntityType.SLIME)) // 80 is a slime
			return;
		
		ChatUtils.message("Slime spawned at: " + (int)packet.getX() + ", "
						+ packet.getY() + ", " + (int)packet.getZ() + ".");
		ChunkPos pos = WurstClient.MC.world.getChunk(new BlockPos(packet.getX(), packet.getY(), packet.getZ())).getPos();
		ChatUtils.message("At chunk: " + pos.x + ", " + pos.z);
	}
	
	

}
