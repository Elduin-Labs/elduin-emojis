package com.elduin.elduin_emojis.mixin;

import com.elduin.elduin_emojis.Emojis;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Every line that shows up in chat, from a player or from the game, ends up in
 * this one addMessage. Swap the emoji codes for faces on the way in.
 *
 * The full signature matters. On 1.21.11 there is also a short addMessage(Component)
 * that player chat never uses, and matching by name alone only hooks that one in
 * the real game.
 */
@Mixin(ChatComponent.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public abstract class ChatComponentMixin {

	//? if >=26.2 {
	/*@ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V", at = @At("HEAD"), argsOnly = true)
	*///?} else {
	@ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), argsOnly = true)
	//?}
	private Component elduinEmojis$swapCodes(Component message) {
		return Emojis.swapCodes(message);
	}
}
