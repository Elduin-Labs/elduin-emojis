package com.elduin.elduin_emojis.platform.fabric;

//? fabric {

import com.elduin.elduin_emojis.ElduinEmojis;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ElduinEmojis.onInitialize();
	}
}
//?}
