/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.wurstclient.WurstClient;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.ChatInputListener.ChatInputEvent;
import net.wurstclient.events.GUIRenderListener.GUIRenderEvent;

@Mixin(InGameHud.class)
public class IngameHudMixin extends DrawableHelper
{
	@Shadow
	private ChatHud chatHud;
	@Shadow
	private MinecraftClient client;
	@Shadow
	private Map<MessageType, List<ClientChatListener>> listeners;
	
	@Inject(
		at = {@At(value = "INVOKE",
			target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
			ordinal = 4)},
		method = {"render(Lnet/minecraft/client/util/math/MatrixStack;F)V"})
	private void onRender(MatrixStack matrixStack, float partialTicks,
		CallbackInfo ci)
	{
		if(WurstClient.MC.options.debugEnabled)
			return;
		
		GUIRenderEvent event = new GUIRenderEvent(matrixStack, partialTicks);
		EventManager.fire(event);
	}
	
	@Inject(at = {@At("HEAD")},
		method = {"renderOverlay(Lnet/minecraft/util/Identifier;F)V"},
		cancellable = true)
	private void onRenderOverlay(Identifier identifier, float scale,
		CallbackInfo ci)
	{
		if(identifier == null || identifier.getPath() == null)
			return;
		
		if(!identifier.getPath().equals("textures/misc/pumpkinblur.png"))
			return;
		
		if(!WurstClient.INSTANCE.getHax().noPumpkinHack.isEnabled())
			return;
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"),
		method = "addChatMessage",
		cancellable = true)
	private void onAddMessage(MessageType type, Text message, UUID sender, CallbackInfo ci)
	{
		if (this.client.shouldBlockMessages(sender)) {
            return;
        }
        if (this.client.options.hideMatchedNames && this.client.shouldBlockMessages(shadow$extractSender(message))) {
            return;
        }
        
        if (!sender.equals(WurstClient.MC.player.getUuid())) {
			ChatInputEvent event = new ChatInputEvent(message, chatHud.getMessageHistory());
			
			EventManager.fire(event);
			if(event.isCancelled())
			{
				ci.cancel();
				return;
			}
        }
		
        for (ClientChatListener clientChatListener : this.listeners.get((Object)type)) {
            clientChatListener.onChatMessage(type, message, sender);
        }
		
		ci.cancel();
	}
	
	@Shadow
	public UUID shadow$extractSender(Text message) {
		return null;
    }
}
