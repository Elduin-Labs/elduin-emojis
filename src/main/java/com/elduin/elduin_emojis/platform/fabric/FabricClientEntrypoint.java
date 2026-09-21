package com.elduin.elduin_emojis.platform.fabric;

//? fabric {

import com.elduin.elduin_emojis.ElduinEmojis;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ElduinEmojis.onInitializeClient();
	}

}
//?}
