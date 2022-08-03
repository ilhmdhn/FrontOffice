package com.ihp.frontoffice.view.component;

import java.util.ArrayList;
import java.util.List;

public class BasePagination<T> {

    private static int TOTAL_NUM_ITEMS;
    private static int ITEMS_PER_PAGE = 6;
    private List<T> allData;

    /*
    - Our constructor class.
     */
    public BasePagination(List<T> allData) {
        this.allData = allData;
        this.TOTAL_NUM_ITEMS = allData.size();
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.ITEMS_PER_PAGE = itemsPerPage;
    }

    /*
        TOTAL NUMBER OF PAGES
         */
    public int getTotalPages() {
       /* int remainingItems = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if (remainingItems > 0) {
            double countPages = Math.ceil(TOTAL_NUM_ITEMS / ITEMS_PER_PAGE);
            System.err.println("itung bulang " + countPages);
            return (int) countPages;
        }*/
        int remainingItems = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if(TOTAL_NUM_ITEMS<=ITEMS_PER_PAGE ){
            return 1;
        }
        if(remainingItems==0 && (TOTAL_NUM_ITEMS>ITEMS_PER_PAGE)){
            return TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
        }
        if (remainingItems > 0) {
            double countPages = Math.ceil(TOTAL_NUM_ITEMS / ITEMS_PER_PAGE);
            //System.err.println("itung bulang " + countPages);
            return (int) countPages+1;
        } else {
            return 0;
        }


    }


    public ArrayList<T> getCurrentData(int currentPage) {
        int startItem = currentPage * ITEMS_PER_PAGE;
        int lastItem = startItem + ITEMS_PER_PAGE;

        ArrayList<T> currentData = new ArrayList<>();

        //LOOP THRU LIST OF GALAXIES AND FILL CURRENTGALAXIES LIST
        try {
            for (int i = 0; i < TOTAL_NUM_ITEMS; i++) {

                //ADD CURRENT PAGE'S DATA
                if (i >= startItem && i < lastItem) {
                    currentData.add(allData.get(i));
                }
            }
            return currentData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
