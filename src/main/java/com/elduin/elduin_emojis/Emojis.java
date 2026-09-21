package com.elduin.elduin_emojis;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.brigadier.context.CommandContext;
//? if >=26.2 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
//?}
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

/**
 * Turns :red:, :blue: and friends into little pictures of Elduin's face.
 *
 * Each emoji is one character in the mod's own font (assets/elduin_emojis/font/emoji.json),
 * so a chat line can mix normal letters and faces.
 */
public final class Emojis {

	public static final FontDescription FONT = new FontDescription.Resource(ElduinEmojis.id("emoji"));

	/** The code you type, and its character in the emoji font. Same order as textures/font/emojis.png. */
	public static final Map<String, Character> CODES = new LinkedHashMap<>();

	static {
		String[] names = {"elduin", "red", "orange", "yellow", "green", "aqua", "blue", "purple", "pink"};
		for (int i = 0; i < names.length; i++) {
			CODES.put(names[i], (char) (0xE000 + i));
		}
	}

	private static final Pattern CODE = Pattern.compile(":([A-Za-z]+):");

	private Emojis() {
	}

	/** Returns the message with every emoji code swapped for its face, or the message itself if it has none. */
	public static Component swapCodes(Component message) {
		MutableComponent out = Component.empty();
		boolean[] swapped = {false};

		message.visit((style, text) -> {
			swapped[0] |= appendSwapped(out, style, text);
			return Optional.empty();
		}, Style.EMPTY);

		return swapped[0] ? out : message;
	}

	private static boolean appendSwapped(MutableComponent out, Style style, String text) {
		Matcher m = CODE.matcher(text);
		boolean swapped = false;
		int from = 0;
		int copied = 0;

		while (from < text.length() && m.find(from)) {
			Character glyph = CODES.get(m.group(1).toLowerCase(Locale.ROOT));

			if (glyph == null) {
				// not one of ours; its closing colon might open a real one, as in ":hi:red:"
				from = m.end() - 1;
				continue;
			}

			if (m.start() > copied) {
				out.append(Component.literal(text.substring(copied, m.start())).setStyle(style));
			}
			out.append(face(glyph, style));
			swapped = true;
			copied = m.end();
			from = m.end();
		}

		if (copied < text.length()) {
			out.append(Component.literal(text.substring(copied)).setStyle(style));
		}
		return swapped;
	}

	/** White and shadowless, so gray or colored chat doesn't tint the face. */
	private static Component face(char glyph, Style around) {
		return Component.literal(String.valueOf(glyph))
				.setStyle(around.withFont(FONT).withColor(ChatFormatting.WHITE).withShadowColor(0));
	}

	/** /emojis shows every face and the code that makes it. */
	public static void registerListCommand() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				//? if >=26.2 {
				/*dispatcher.register(ClientCommands.literal("emojis").executes(Emojis::list)));
				*///?} else {
				dispatcher.register(ClientCommandManager.literal("emojis").executes(Emojis::list)));
				//?}
	}

	private static int list(CommandContext<FabricClientCommandSource> context) {
		context.getSource().sendFeedback(Component.translatable("message.elduin_emojis.list"));

		MutableComponent line = Component.empty();
		int onLine = 0;
		for (Map.Entry<String, Character> entry : CODES.entrySet()) {
			line.append(face(entry.getValue(), Style.EMPTY));
			// The code is split into two pieces on purpose. Chat swaps codes as the
			// line comes in, and a whole ":red:" here would turn into the face too.
			line.append(Component.literal(" :").withStyle(ChatFormatting.YELLOW));
			line.append(Component.literal(entry.getKey() + ":   ").withStyle(ChatFormatting.YELLOW));

			if (++onLine == 3) {
				context.getSource().sendFeedback(line);
				line = Component.empty();
				onLine = 0;
			}
		}
		if (onLine > 0) {
			context.getSource().sendFeedback(line);
		}
		return 1;
	}
}
