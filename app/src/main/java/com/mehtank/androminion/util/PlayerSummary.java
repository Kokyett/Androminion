package com.mehtank.androminion.util;

import com.vdom.comms.GameStatus.JourneyTokenState;

/**
 * Information about one player
 * 
 * deck, hand size, total number of cards etc.
 */

public class PlayerSummary {
	private static final String TAG = "PlayerSummary";
	
	public String name;
	public String realName;
	public int deckSize;
	public boolean stashOnDeck;
	public int handSize;
	public int stashesInHand;
	public int numCards;
	public int vp;
	public int pt;
	public int vt;
	public int dt;
	public int gct; // Guilds Coin Tokens (Coffers)
	public int villagers;
	public JourneyTokenState journeyToken;
	public boolean minusOneCoinTokenOn;
	public boolean minusOneCardTokenOn;
	public boolean hasDeluded;
	public boolean hasEnvious;
	public boolean hasLostInTheWoods;
	public boolean hasMiserable;
	public boolean hasTwiceMiserable;
	public boolean hasFlag;
	public boolean hasHorn;
	public boolean hasKey;
	public boolean hasLantern;
	public boolean hasTreasureChest;
	public boolean highlight = false;
	public boolean showColor = false;
	public int color;
	public int turns;
	
	public PlayerSummary(String name) {
		this.name = name;
	}
	
	public void set(String name, int turns, int deckSize, boolean stashOnDeck, int handSize, int stashesInHand, int numCards, int vp, int pt, int vt, int dt, int gct, int villagers, 
			boolean minusOneCoinTokenOn, boolean minusOneCardTokenOn, JourneyTokenState journeyToken,
			boolean hasDeluded, boolean hasEnvious, boolean hasLostInTheWoods, boolean hasMiserable, boolean hasTwiceMiserable,
			boolean hasFlag, boolean hasHorn, boolean hasKey, boolean hasLantern, boolean hasTreasureChest,
			boolean highlight, boolean showColor, int color){
		this.name = name;
		this.turns = turns;
		this.deckSize = deckSize;
		this.stashOnDeck = stashOnDeck;
		this.handSize = handSize;
		this.stashesInHand = stashesInHand;
		this.numCards = numCards;
		this.vp = vp;
		this.pt = pt;
		this.vt = vt;
		this.dt = dt;
		this.gct = gct;
		this.villagers = villagers;
		this.journeyToken = journeyToken;
		this.minusOneCoinTokenOn = minusOneCoinTokenOn;
		this.minusOneCardTokenOn = minusOneCardTokenOn;
		this.hasDeluded = hasDeluded;
		this.hasEnvious = hasEnvious;
		this.hasLostInTheWoods = hasLostInTheWoods;
		this.hasMiserable = hasMiserable;
		this.hasTwiceMiserable = hasTwiceMiserable;
		this.hasFlag = hasFlag;
		this.hasHorn = hasHorn;
		this.hasKey = hasKey;
		this.hasLantern = hasLantern;
		this.hasTreasureChest = hasTreasureChest;
		this.highlight = highlight;
		this.showColor = showColor;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return name; 
	}
}