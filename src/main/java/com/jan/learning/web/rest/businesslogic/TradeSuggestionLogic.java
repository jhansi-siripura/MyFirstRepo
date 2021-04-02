package com.jan.learning.web.rest.businesslogic;

import com.jan.learning.domain.TradeSuggestion;
import com.jan.learning.domain.enumeration.TradeAction;
import com.jan.learning.domain.enumeration.TradeStatus;
import com.jan.learning.domain.enumeration.TradeType;
import java.time.Duration;
import java.util.Random;

public class TradeSuggestionLogic {

    //TODO get the correct range doing meditation
    private static final int MIN_PROFIT_CO = 8;
    private static final int MAX_PROFIT_CO = 15;

    private static final int MIN_PROFIT_MIS = 10;
    private static final int MAX_PROFIT_MIS = 30;

    private static final int MIN_PROFIT_NRML = 20;
    private static final int MAX_PROFIT_NRML = 100;

    //TODO get the correct range doing meditation and calculation
    private static final int MIN_SL_CO = 28;
    private static final int MAX_SL_CO = 32;

    private static final int MIN_SL_MIS = 28;
    private static final int MAX_SL_MIS = 50;

    private static final int MIN_SL_NRML = 28;
    private static final int MAX_SL_NRML = 100;

    private static final int LUCKY_BUY_DIGIT = 5;
    private static final int LUCKY_SELL_DIGIT = 6;

    public static void main(String[] args) {
        System.out.println(
            "Archagel Ariel please suggest me how to trade and earn money quickly and easily.Thank you.Thank you.Thank you."
        );
        TradeSuggestionLogic logic = new TradeSuggestionLogic();

        TradeSuggestion input = new TradeSuggestion();
        input.setCurrentMarketPrice(4444);
        input.setMinProfitPoints(13);

        TradeSuggestion output = logic.getSuggestionFromAngel(input);

        System.out.println(output);
    }

    public TradeSuggestion getSuggestionFromAngel(TradeSuggestion tradeSuggestion) {
        int cmp = tradeSuggestion.getCurrentMarketPrice();
        TradeAction action = randomTradeAction();
        tradeSuggestion.setAction(action);

        if (action == TradeAction.BUY) {
            tradeSuggestion = processBuyAction(tradeSuggestion);
        } else if (action == TradeAction.SELL) {
            tradeSuggestion = processSellAction(tradeSuggestion);
        } else {
            tradeSuggestion = processWaitAction(tradeSuggestion);
        }

        return tradeSuggestion;
    }

    private TradeType randomTradeType() {
        int pick = new Random().nextInt(TradeType.values().length);
        return TradeType.values()[pick];
    }

    private TradeAction randomTradeAction() {
        int pick = new Random().nextInt(TradeAction.values().length);
        return TradeAction.values()[pick];
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private TradeSuggestion processBuyAction(TradeSuggestion tradeSuggestion) {
        int tradeInBuyPrice = calulateTradeInBuyPrice(tradeSuggestion.getCurrentMarketPrice());
        tradeSuggestion.setTradeType(randomTradeType());
        String tradeType = tradeSuggestion.getTradeType().toString();

        int minProfitPoints = tradeSuggestion.getMinProfitPoints();
        int minTargetPice = tradeInBuyPrice + minProfitPoints;
        int betterProfitPoints = determineProfitPoints(tradeType);
        int betterTargetPice = tradeInBuyPrice + betterProfitPoints;
        int slUnits = determineSLunits(tradeType);
        int slPrice = tradeInBuyPrice - slUnits;

        tradeSuggestion.setTradeInPrice(tradeInBuyPrice);
        tradeSuggestion.setMinTradeOutPrice(minTargetPice);
        tradeSuggestion.setBetterTradeoutPrice(betterProfitPoints);
        tradeSuggestion.setBetterTradeoutPrice(betterTargetPice);
        tradeSuggestion.setSlPoints(slUnits);
        tradeSuggestion.setSlPrice(slPrice);
        //TODO need to set more fileds
        tradeSuggestion.setTradeStatus(TradeStatus.PENDING);

        return tradeSuggestion;
    }

    private TradeSuggestion processSellAction(TradeSuggestion tradeSuggestion) {
        int tradeInSellPrice = calulateTradeInSellPrice(tradeSuggestion.getCurrentMarketPrice());
        tradeSuggestion.setTradeType(randomTradeType());
        String tradeType = tradeSuggestion.getTradeType().toString();

        int minProfitPoints = tradeSuggestion.getMinProfitPoints();
        int minTargetPice = tradeInSellPrice - minProfitPoints;
        int betterProfitPoints = determineProfitPoints(tradeType);
        int betterTargetPice = tradeInSellPrice - betterProfitPoints;
        int slUnits = determineSLunits(tradeType);
        int slPrice = tradeInSellPrice + slUnits;

        tradeSuggestion.setTradeInPrice(tradeInSellPrice);
        tradeSuggestion.setMinTradeOutPrice(minTargetPice);
        tradeSuggestion.setBetterTradeoutPrice(betterProfitPoints);
        tradeSuggestion.setBetterTradeoutPrice(betterTargetPice);
        tradeSuggestion.setSlPoints(slUnits);
        tradeSuggestion.setSlPrice(slPrice);

        //TODO need to set more fileds
        tradeSuggestion.setTradeStatus(TradeStatus.PENDING);

        return tradeSuggestion;
    }

    private TradeSuggestion processWaitAction(TradeSuggestion tradeSuggestion) {
        long durutaion_Minutes = (getRandomNumber(5, 22));
        Duration waitDuration = Duration.ofMinutes(durutaion_Minutes);
        //          tradeSuggestion.setWaitDuration(waitDuration);
        tradeSuggestion.setTradeStatus(TradeStatus.PENDING);
        tradeSuggestion.setMinProfitPoints(null);

        return tradeSuggestion;
    }

    private int calulateTradeInBuyPrice(int currentMarketPrice) {
        int digitCount = getDigitCount(currentMarketPrice);
        int diff = 0;
        if (digitCount == LUCKY_BUY_DIGIT) {
            diff = 0;
        } else if (digitCount > LUCKY_BUY_DIGIT) {
            diff = digitCount - LUCKY_BUY_DIGIT;
        } else {
            diff = 9 - (LUCKY_BUY_DIGIT - digitCount);
        }
        return currentMarketPrice - diff;
    }

    private int calulateTradeInSellPrice(int currentMarketPrice) {
        int digitCount = getDigitCount(currentMarketPrice);
        int diff = 0;
        if (digitCount == LUCKY_SELL_DIGIT) {
            diff = 0;
        } else if (digitCount > LUCKY_SELL_DIGIT) {
            diff = 9 + (LUCKY_SELL_DIGIT - digitCount);
        } else {
            diff = (LUCKY_SELL_DIGIT) - digitCount;
        }
        return currentMarketPrice + diff;
    }

    private int getDigitCount(int currentPrice) {
        int sum = 0;
        int digit = 0;
        while (currentPrice > 0) {
            digit = currentPrice % 10;
            sum = sum + digit;
            currentPrice = (currentPrice / 10);
        }
        if (sum > 9) {
            sum = getDigitCount(sum);
        }
        return sum;
    }

    private int determineProfitPoints(String tradeActionType) {
        int profitPoints = 0;

        if (TradeType.CO.equals(tradeActionType)) {
            profitPoints = getRandomNumber(MIN_PROFIT_CO, MAX_PROFIT_CO);
        } else if (TradeType.MIS.equals(tradeActionType)) {
            profitPoints = getRandomNumber(MIN_PROFIT_MIS, MAX_PROFIT_MIS);
        } else {
            profitPoints = getRandomNumber(MIN_PROFIT_NRML, MAX_PROFIT_NRML);
        }
        return profitPoints;
    }

    private int determineSLunits(String tradeActionType) {
        int slUnits = 0;

        if (TradeType.CO.equals(tradeActionType)) {
            slUnits = getRandomNumber(MIN_SL_CO, MAX_SL_CO);
        } else if (TradeType.MIS.equals(tradeActionType)) {
            slUnits = getRandomNumber(MIN_SL_MIS, MAX_SL_MIS);
        } else {
            slUnits = getRandomNumber(MIN_SL_NRML, MAX_SL_NRML);
        }
        return slUnits;
    }
}
