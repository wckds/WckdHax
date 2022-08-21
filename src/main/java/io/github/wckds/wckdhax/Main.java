package io.github.wckds.wckdhax;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		AttackBlockCallback.EVENT.register(new BlockClickManager());

		new Settings(); // instantiating Settings so that static variables are initialized.

		System.out.println("Loaded WckdHax.");
	}
}
