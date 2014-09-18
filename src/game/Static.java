package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import effects.hero.HeroEffect;

public class Static {
	static Target attacker = null;
	static int index = 200;
	public static Random ramdom = new Random();

	static ArrayList<HeroEffect> endTurn = new ArrayList<HeroEffect>();
	static ArrayList<HeroEffect> startTurn = new ArrayList<HeroEffect>();
	static ArrayList<HeroEffect> onMonsterSpawned = new ArrayList<HeroEffect>();
	static ArrayList<HeroEffect> onCardUsed = new ArrayList<HeroEffect>();

	static Stack<Card> use = new Stack<Card>();
	
	public void runEndTurnEffects() {
		if (endTurn.size() == 0)
			return;
 
		for (HeroEffect effect : endTurn) {
			effect.run(0);
		}
	} 

	public void runStartTurnEffects() {
		if (startTurn.size() == 0)
			return;

		
		for (HeroEffect effect : startTurn) {
			effect.run(0);
		}
	}

	public void runOnMonsterSpawnedEffects() {
		if (onMonsterSpawned.size() == 0)
			return;

		
		for (HeroEffect effect : onMonsterSpawned) {
			effect.run(0);
		}
	}

	public void runCardUsedEffects() {
		if (onCardUsed.size() == 0)
			return;
		
		for (HeroEffect effect : onCardUsed) {
			effect.run(0);
		}
	}
	
	
	

	public static void Cancel(Player player, boolean sendToAnother) {
		player.endTurnReset();
		player.attackCheck();

		player.attackReady();

		player.enemy.listenerHelper();
		if (sendToAnother) {
			Game.sender.S("12&");
		}
	}
	
	public static void Cancel(Player player) {
		player.endTurnReset();
		player.attackCheck();

		player.attackReady();

		player.enemy.listenerHelper();

	}

	public static int index() {
		index++;
		return index;
	}
}
