package com.syifa.frontend.strategies;

import com.syifa.frontend.Coin;
import com.syifa.frontend.factories.CoinFactory;
import java.util.List;

public interface CoinPattern {
    List<Coin> spawn(CoinFactory factory, float groundTopY, float spawnX, float screenHeight);
    String getName();
}
