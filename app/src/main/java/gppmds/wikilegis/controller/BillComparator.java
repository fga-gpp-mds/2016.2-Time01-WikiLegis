package gppmds.wikilegis.controller;

import java.util.Comparator;

import gppmds.wikilegis.model.Bill;

/**
 * Created by josue on 9/14/16.
 */
public class BillComparator implements Comparator<Bill> {


    @Override
    public int compare(Bill bill, Bill t1) {
       if(bill.getDate()>t1.getDate()){
           return -1;
       }if(bill.getDate()<t1.getDate()){
            return 1;
        }
        return 0;
    }
}
