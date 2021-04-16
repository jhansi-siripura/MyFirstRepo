package com.jan.learning.web.rest.util;

import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;

public class ProfitUtil {

    public static void main(String[] args) {
        int minProfit = 8;
        int maxProfit = 18;
        int grandProfit = 40;

        int profitPoints = getProfitPointsExpected(minProfit, maxProfit, grandProfit);

        System.out.println("Expect Profit points : " + profitPoints);
    }

    public static int getProfitPointsExpected(int minProfit, int maxProfit, int grandProfit) {
        List<Integer> profitChitsList = new ArrayList();
        List<Integer> finalProfitChitsList = new ArrayList();

        int count = 0;
        for (int i = 40; i >= 8; i--) {
            int tmpPaperChit = i;

            if (i >= 18) count++;

            for (int j = 0; j < count; j++) profitChitsList.add(tmpPaperChit);
        }

        //        System.out.println("profitChitsList : "+profitChitsList);
        //        System.out.println("profitChitsList size : "+profitChitsList.size());

        // int size =profitChitsList.size(); //It keeps size constant so more chits
        for (int i = 0; i < profitChitsList.size(); i++) {
            int minRange = 0;
            int maxRange = profitChitsList.size() - 1;

            int randomIndex = RandomUtil.getRandom(minRange, maxRange);

            int profitChit = profitChitsList.get(randomIndex);

            if (profitChit != 0) {
                finalProfitChitsList.add(profitChit);
                //profitChitsList.add(randomIndex,0);
                profitChitsList.remove(randomIndex);
            }
        }

        //        System.out.println("profitChitsList : "+profitChitsList);
        //        System.out.println("profitChitsList size : "+profitChitsList.size());
        //
        //        System.out.println("finalProfitChitsList : "+finalProfitChitsList);
        //        System.out.println("finalProfitChitsList size : "+finalProfitChitsList.size());

        return finalProfitChitsList.get(RandomUtil.getRandom(0, finalProfitChitsList.size() - 1));
    }
}
