package net.wurstclient.hacks;

import java.util.Objects;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.WurstClient;
import net.wurstclient.events.ChatInputListener;
import net.wurstclient.hack.Hack;

@SearchTags({"Name notifier", "name in chat", "playernotifier",
	"Player notifier"})
public class NameNotifierHack extends Hack implements ChatInputListener
{

	public NameNotifierHack()
	{
		super("NameNotifier");
		setCategory(Category.CHAT);
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(ChatInputListener.class, this);
	}

	@Override
	public void onDisable() {
		EVENTS.add(ChatInputListener.class, this);
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event) {
		String message = event.getComponent().getString().toLowerCase();
		String[] nickNames = {
				WurstClient.MC.player.getName().getString(),
				Objects.requireNonNullElse(WurstClient.MC.player.getCustomName(), WurstClient.MC.player.getName()).getString(),
				WurstClient.MC.player.getDisplayName().getString()
		};

		for (int i = 0; i < nickNames.length; i++)
		{
			if (message.contains(nickNames[i].toLowerCase()))
			{
				WurstClient.MC.world.playSound(WurstClient.MC.player.getBlockPos(),
						SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.MASTER,
						1f, 1f, false);
				return;
			}
		}
	}
	
}
